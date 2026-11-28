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
import kotlinx.coroutines.CancellationException
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

    fun carregar(forcarAtualizacao: Boolean = false) {
        if (!PermissoesAcesso.podeAcessar(SessionManager.role, RecursoSistema.EVOLUCOES_CLINICAS)) return
        if (carregamentoDados?.isActive == true && !forcarAtualizacao) return

        carregamentoDados = viewModelScope.launch {
            try {
                _evolucoes.value = evolucaoApi.carregar()
                _pacientes.value = pacienteApi.carregar()
                _prontuarios.value = prontuarioApi.carregar()
                _psicologos.value = psicologoApi.carregar()
                _agendas.value = agendaApi.carregar()
            } catch (e: CancellationException) {
                throw e
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

    var prontuarioId by mutableStateOf<Long?>(null)
        private set
    var agendaId by mutableStateOf<Long?>(null)
        private set
    var psicologoId by mutableStateOf<Long?>(null)
        private set

    var erroProntuario by mutableStateOf(false)
        private set
    var erroAgenda by mutableStateOf(false)
        private set
    var erroPsicologo by mutableStateOf(false)
        private set

    val dataRegistro = CampoValidado()
    val horaRegistro = CampoValidado()
    val descricao = CampoValidado()
    val ativo = CampoValidado("true")

    fun selecionarProntuario(idSelecionado: Long?) {
        val prontuario = _prontuarios.value.firstOrNull { it.id == idSelecionado }
        if (prontuario != null) {
            selecionarProntuario(prontuario)
        } else {
            prontuarioId = null
            erroProntuario = true
        }
    }

    fun selecionarAgenda(idSelecionado: Long?) {
        val agenda = _agendas.value.firstOrNull { it.id == idSelecionado }
        if (agenda != null) {
            selecionarAgenda(agenda)
        } else {
            agendaId = null
            erroAgenda = true
        }
    }

    fun selecionarPsicologo(idSelecionado: Long?) {
        psicologoId = idSelecionado
        erroPsicologo = idSelecionado == null
    }

    fun selecionarPaciente(nomePaciente: String) {
        val paciente = _pacientes.value.firstOrNull { it.nome == nomePaciente }
        if (paciente == null) {
            _pacienteIdFiltro.value = null
            _filtro.value = nomePaciente
            prontuarioId = null
            agendaId = null
            psicologoId = null
            erroProntuario = false
            erroAgenda = false
            erroPsicologo = false
            return
        }
        selecionarPaciente(paciente)
    }

    fun selecionarPaciente(paciente: Paciente) {
        _pacienteIdFiltro.value = paciente.id
        _filtro.value = paciente.nome
        val prontuariosDoPaciente = _prontuarios.value.filter {
            it.pacienteId == paciente.id
        }
        if (prontuariosDoPaciente.size == 1) {
            selecionarProntuario(prontuariosDoPaciente.first())
        } else {
            prontuarioId = null
            agendaId = null
            psicologoId = null
            erroProntuario = false
            erroAgenda = false
            erroPsicologo = false
        }
    }

    fun selecionarAgenda(agenda: Agenda) {
        agendaId = agenda.id
        erroAgenda = false

        psicologoId = agenda.psicologoId
        erroPsicologo = false

        val prontuariosDoPaciente = _prontuarios.value.filter {
            it.pacienteId == agenda.pacienteId
        }
        val prontuarioAtualValido = prontuarioId?.takeIf { idAtual ->
            prontuariosDoPaciente.any { it.id == idAtual }
        }
        prontuarioId = prontuarioAtualValido ?: prontuariosDoPaciente.singleOrNull()?.id
        erroProntuario = false
    }

    fun selecionarProntuario(prontuario: Prontuario) {
        prontuarioId = prontuario.id
        erroProntuario = false

        val agendasDoPaciente = _agendas.value.filter { it.pacienteId == prontuario.pacienteId }
        val agendaAtual = agendasDoPaciente.firstOrNull { it.id == agendaId }
            ?: agendasDoPaciente.singleOrNull()
        agendaId = agendaAtual?.id
        erroAgenda = false

        psicologoId = agendaAtual?.psicologoId
        erroPsicologo = false
    }

    fun salvar() {
        if (salvando) return

        val psicologoDaAgenda = _agendas.value
            .firstOrNull { it.id == agendaId }
            ?.psicologoId
            ?: psicologoId
        if (psicologoDaAgenda != null) {
            psicologoId = psicologoDaAgenda
        }

        val prontuarioSelecionado = prontuarioId
        val agendaSelecionada = agendaId
        val psicologoSelecionado = psicologoId

        erroProntuario = prontuarioSelecionado == null
        erroAgenda = agendaSelecionada == null
        erroPsicologo = psicologoSelecionado == null

        val v1 = dataRegistro.validar()
        val v2 = descricao.validar()
        val v3 = ativo.validar()

        val fksValidas = !erroProntuario && !erroAgenda && !erroPsicologo
        val camposValidos = v1 && v2 && v3

        if (!fksValidas || !camposValidos) {
            mandarMensagem(
                if (!fksValidas && camposValidos) {
                    "Selecione Prontuário, Agenda e Psicólogo."
                } else {
                    "Preencha todos os campos obrigatórios."
                }
            )
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val hora = horaRegistro.valor.value.trim().ifEmpty { "00:00" }
                val dataIso = formatarDataBrasileiraParaIso(dataRegistro.valor.value.trim())

                val evolucao = EvolucaoClinica(
                    id = id.value.toLongOrNull(),
                    prontuarioId = prontuarioSelecionado!!,
                    psicologoId = psicologoSelecionado!!,
                    agendaId = agendaSelecionada!!,
                    dataRegistro = "${dataIso}T${hora}:00",
                    descricao = descricao.valor.value.trim(),
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    evolucaoApi.salvar(evolucao)
                } else {
                    val idNumber = id.value.toLong()
                    evolucaoApi.atualizar(idNumber, evolucao)
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
        if (backStack.lastOrNull() != EvolucaoClinicaRotas.Formulario) {
            backStack.add(EvolucaoClinicaRotas.Formulario)
        }
        id.value = ""
        limparCampos()
        carregar(forcarAtualizacao = true)
        if (nomePaciente.isNotEmpty()) {
            selecionarPaciente(nomePaciente)
        }
    }

    fun editar(evolucao: EvolucaoClinica) {
        if (backStack.lastOrNull() != EvolucaoClinicaRotas.Formulario) {
            backStack.add(EvolucaoClinicaRotas.Formulario)
        }
        id.value = evolucao.id?.toString() ?: ""
        prontuarioId = evolucao.prontuarioId
        agendaId = evolucao.agendaId
        psicologoId = evolucao.psicologoId
        erroProntuario = false
        erroAgenda = false
        erroPsicologo = false
        carregar(forcarAtualizacao = true)

        val partesDataHora = evolucao.dataRegistro.split('T', limit = 2)
        dataRegistro.definir(formatarDataIsoParaBrasileira(partesDataHora[0]))
        horaRegistro.definir(partesDataHora.getOrNull(1)?.take(5).orEmpty())
        descricao.definir(evolucao.descricao.orEmpty())
        ativo.definir(evolucao.ativo.toString())
    }

    fun limparCampos() {
        prontuarioId = null
        psicologoId = null
        agendaId = null
        erroProntuario = false
        erroAgenda = false
        erroPsicologo = false
        dataRegistro.limpar()
        horaRegistro.limpar()
        descricao.limpar()
        ativo.definir("true")
    }

    fun nomeProntuario(id: Long): String {
        val prontuario = _prontuarios.value.firstOrNull { it.id == id } ?: return "Prontuário"
        val nomePaciente = _pacientes.value.firstOrNull { it.id == prontuario.pacienteId }?.nome
            ?: "Paciente"
        return "$nomePaciente - ${prontuario.queixaPrincipal}"
    }

    fun nomePsicologo(id: Long): String =
        _psicologos.value.firstOrNull { it.id == id }?.nome ?: "Psicólogo"

    fun nomeAgenda(id: Long): String {
        val agenda = _agendas.value.firstOrNull { it.id == id } ?: return "Agenda"
        val paciente = _pacientes.value.firstOrNull { it.id == agenda.pacienteId }?.nome
            ?: "Paciente"
        return "$paciente - ${formatarDataHoraIsoParaBrasileira(agenda.dataHoraInicio)}"
    }
}