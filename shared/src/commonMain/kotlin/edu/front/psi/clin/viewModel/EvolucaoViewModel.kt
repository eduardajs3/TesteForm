package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.*
import edu.front.psi.clin.model.*
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.formatarDataBrasileiraParaIso
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EvolucaoClinicaViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val evolucaoApi = EvolucaoClinicaAPI(httpClient)
    private val pacienteApi = PacienteAPI(httpClient)
    private val prontuarioApi = ProntuarioAPI(httpClient)
    private val psicologoApi = PsicologoAPI(httpClient)
    private val agendaApi = AgendaAPI(httpClient)

    private val _evolucoes = MutableStateFlow<List<EvolucaoClinica>>(emptyList())
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    val pacientes = _pacientes.asStateFlow()

    private val _prontuarios = MutableStateFlow<List<Prontuario>>(emptyList())
    val prontuarios = _prontuarios.asStateFlow()

    private val _psicologos = MutableStateFlow<List<Psicologo>>(emptyList())
    val psicologos = _psicologos.asStateFlow()

    private val _agendas = MutableStateFlow<List<Agenda>>(emptyList())
    val agendas = _agendas.asStateFlow()

    private val _pacienteIdFiltro = MutableStateFlow<Long?>(null)
    val pacienteIdFiltro = _pacienteIdFiltro.asStateFlow()

    private var carregamentoDados: Job? = null

    var salvando by mutableStateOf(false)
        private set

    init {
        carregar()
    }

    fun carregar() {
        if (!PermissoesAcesso.podeAcessar(SessionManager.role, RecursoSistema.EVOLUCOES_CLINICAS)) return
        if (carregamentoDados?.isActive == true) return

        carregamentoDados = viewModelScope.launch {
            try {
                _evolucoes.value = evolucaoApi.carregar()
                _pacientes.value = pacienteApi.carregar()
                _prontuarios.value = prontuarioApi.carregar()
                _psicologos.value = psicologoApi.carregar()
                _agendas.value = agendaApi.carregar()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar dados da evolução clínica"))
            } finally {
                carregamentoDados = null
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro.trim()
        _pacienteIdFiltro.value = null
    }

    val evolucoesFiltradas = combine(
        _evolucoes,
        _filtro,
        _prontuarios,
        _pacientes,
        _pacienteIdFiltro
    ) { evolucoes, filtro, prontuarios, pacientes, pacienteIdFiltro ->
        if (pacienteIdFiltro != null) {
            evolucoes.filter { evolucao ->
                prontuarios.any {
                    it.id == evolucao.prontuarioId && it.pacienteId == pacienteIdFiltro
                }
            }
        } else if (filtro.isEmpty()) {
            evolucoes
        } else {
            evolucoes.filter {
                val prontuario = prontuarios.firstOrNull { prontuario -> prontuario.id == it.prontuarioId }
                val nomePaciente = pacientes.firstOrNull { paciente ->
                    paciente.id == prontuario?.pacienteId
                }?.nome.orEmpty()
                nomePaciente.contains(filtro, ignoreCase = true) ||
                    it.prontuarioId.toString().contains(filtro, ignoreCase = true) ||
                    it.psicologoId.toString().contains(filtro, ignoreCase = true) ||
                    it.descricao?.contains(filtro, ignoreCase = true) == true
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<EvolucaoClinicaRotas>(EvolucaoClinicaRotas.Listagem)

    val id = mutableStateOf("")
    val prontuarioId = CampoValidado()
    val psicologoId = CampoValidado()
    val agendaId = CampoValidado()

    val dataRegistro = CampoValidado()
    val horaRegistro = CampoValidado()
    val descricao = CampoValidado()
    val ativo = CampoValidado("true")

    fun salvar() {
        if (salvando) return

        val agendaIdAtual = agendaId.valor.value.toLongOrNull()
        val psicologoDaAgenda = _agendas.value
            .firstOrNull { it.id == agendaIdAtual }
            ?.psicologoId
            ?: psicologoId.valor.value.toLongOrNull()
        if (psicologoDaAgenda != null && psicologoId.valor.value != psicologoDaAgenda.toString()) {
            psicologoId.definir(psicologoDaAgenda.toString())
        }

        val vProntuario = prontuarioId.validar()
        val vAgenda = agendaId.validar()
        val vPsicologo = psicologoId.validar()
        val v1 = dataRegistro.validar()
        val v2 = descricao.validar()
        val v3 = ativo.validar()

        val prontuarioSelecionado = prontuarioId.valor.value.toLongOrNull()
        val agendaSelecionada = agendaId.valor.value.toLongOrNull()
        val psicologoSelecionado = psicologoDaAgenda

        if (!(vProntuario && vAgenda && vPsicologo && v1 && v2 && v3)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        if (prontuarioSelecionado == null || agendaSelecionada == null || psicologoSelecionado == null) {
            mandarMensagem("Selecione Prontuário, Agenda e Psicólogo válidos.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val idAtual = id.value.toLongOrNull()
                val horaFormatada = horaRegistro.valor.value.trim().ifEmpty { "00:00" }
                val dataIso = formatarDataBrasileiraParaIso(dataRegistro.valor.value.trim())

                val evolucao = EvolucaoClinica(
                    id = idAtual,
                    prontuarioId = prontuarioSelecionado,
                    psicologoId = psicologoSelecionado,
                    agendaId = agendaSelecionada,
                    dataRegistro = "${dataIso}T${horaFormatada}:00",
                    descricao = descricao.valor.value.trim(),
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    evolucaoApi.salvar(evolucao)
                } else {
                    evolucaoApi.atualizar(idAtual!!, evolucao)
                }

                _evolucoes.value = evolucaoApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Evolução clínica salva com sucesso!"
                } else {
                    "Evolução clínica atualizada com sucesso!"
                }

                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(EvolucaoClinicaRotas.Listagem)
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar evolução clínica"))
            } finally {
                salvando = false
            }
        }
    }

    fun apagar(evolucao: EvolucaoClinica) {
        viewModelScope.launch {
            try {
                evolucaoApi.apagar(evolucao)
                _evolucoes.value = evolucaoApi.carregar()
                mandarMensagem("Evolução clínica apagada com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar evolução clínica"))
            }
        }
    }

    fun novaEvolucao(nomePaciente: String = "") {
        backStack.clear()
        backStack.add(EvolucaoClinicaRotas.Formulario)
        id.value = ""
        limparCampos()
        if (nomePaciente.isNotEmpty()) {
            selecionarPaciente(nomePaciente)
        }
    }

    fun selecionarPaciente(nomePaciente: String) {
        val paciente = _pacientes.value.firstOrNull { it.nome == nomePaciente }
        if (paciente == null) {
            _pacienteIdFiltro.value = null
            _filtro.value = nomePaciente
            prontuarioId.limpar()
            agendaId.limpar()
            psicologoId.limpar()
            return
        }
        selecionarPaciente(paciente)
    }

    fun selecionarPaciente(paciente: Paciente) {
        _pacienteIdFiltro.value = paciente.id
        _filtro.value = paciente.nome
        val prontuario = _prontuarios.value.firstOrNull {
            it.pacienteId == paciente.id
        }
        if (prontuario != null) {
            selecionarProntuario(prontuario)
        } else {
            prontuarioId.limpar()
            agendaId.limpar()
            psicologoId.limpar()
        }
    }

    fun selecionarAgenda(agenda: Agenda) {
        agendaId.definir(agenda.id?.toString().orEmpty())
        psicologoId.definir(agenda.psicologoId.toString())

        val prontuariosDoPaciente = _prontuarios.value.filter {
            it.pacienteId == agenda.pacienteId
        }
        val prontuarioAtualId = prontuarioId.valor.value.toLongOrNull()?.takeIf { id ->
            prontuariosDoPaciente.any { it.id == id }
        }
        val prontuarioResolvido = prontuarioAtualId ?: prontuariosDoPaciente.singleOrNull()?.id
        prontuarioId.definir(prontuarioResolvido?.toString().orEmpty())
    }

    fun selecionarProntuario(prontuario: Prontuario) {
        prontuarioId.definir(prontuario.id?.toString().orEmpty())
        val agendaAtualId = agendaId.valor.value.toLongOrNull()
        val agendaAtual = _agendas.value.firstOrNull {
            it.id == agendaAtualId && it.pacienteId == prontuario.pacienteId
        }
        agendaId.definir(agendaAtual?.id?.toString().orEmpty())
        psicologoId.definir(agendaAtual?.psicologoId?.toString().orEmpty())
    }

    fun editar(evolucao: EvolucaoClinica) {
        backStack.clear()
        backStack.add(EvolucaoClinicaRotas.Formulario)
        id.value = evolucao.id?.toString() ?: ""
        prontuarioId.definir(evolucao.prontuarioId.toString())
        agendaId.definir(evolucao.agendaId.toString())
        psicologoId.definir(evolucao.psicologoId.toString())
        _pacienteIdFiltro.value = null
        _filtro.value = ""

        val partesDataHora = evolucao.dataRegistro.split('T', limit = 2)
        dataRegistro.definir(formatarDataIsoParaBrasileira(partesDataHora[0]))
        horaRegistro.definir(partesDataHora.getOrNull(1)?.take(5).orEmpty())
        descricao.definir(evolucao.descricao.orEmpty())
        ativo.definir(evolucao.ativo.toString())
    }

    fun limparCampos() {
        prontuarioId.limpar()
        psicologoId.limpar()
        agendaId.limpar()
        dataRegistro.limpar()
        horaRegistro.limpar()
        descricao.limpar()
        ativo.definir("true")
    }

    fun nomeProntuario(id: Long): String {
        val prontuario = _prontuarios.value.firstOrNull { it.id == id } ?: return "Prontuário #$id"
        val nomePaciente = _pacientes.value.firstOrNull { it.id == prontuario.pacienteId }?.nome
            ?: "Paciente"
        return "$nomePaciente - ${prontuario.queixaPrincipal} (#$id)"
    }

    fun nomePsicologo(id: Long): String =
        _psicologos.value.firstOrNull { it.id == id }?.nome ?: "Psicólogo #$id"

    fun nomeAgenda(id: Long): String {
        val agenda = _agendas.value.firstOrNull { it.id == id } ?: return "Agenda #$id"
        val paciente = _pacientes.value.firstOrNull { it.id == agenda.pacienteId }?.nome
            ?: "Paciente #${agenda.pacienteId}"
        return "$paciente - ${formatarDataHoraIsoParaBrasileira(agenda.dataHoraInicio)}"
    }
}