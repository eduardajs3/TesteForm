package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.AgendaAPI
import edu.front.psi.clin.api.EvolucaoClinicaAPI
import edu.front.psi.clin.api.PacienteAPI
import edu.front.psi.clin.api.ProntuarioAPI
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.model.EvolucaoClinica
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.model.Prontuario
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira
import edu.front.psi.clin.utils.formatarDataBrasileiraParaIso
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import edu.front.psi.clin.utils.MaskUtils
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
    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    private val _prontuarios = MutableStateFlow<List<Prontuario>>(emptyList())
    private val _psicologos = MutableStateFlow<List<Psicologo>>(emptyList())
    private val _agendas = MutableStateFlow<List<Agenda>>(emptyList())
    
    val pacientes = _pacientes.asStateFlow()
    val prontuarios = _prontuarios.asStateFlow()
    val psicologos = _psicologos.asStateFlow()
    val agendas = _agendas.asStateFlow()
    
    var salvando by mutableStateOf(false)
        private set

    init {
        if (PermissoesAcesso.podeAcessar(SessionManager.role, RecursoSistema.EVOLUCOES_CLINICAS)) {
            carregarDados(evolucaoApi::carregar) { _evolucoes.value = it }
            carregarDados(pacienteApi::carregar) { _pacientes.value = it }
            carregarDados(prontuarioApi::carregar) { _prontuarios.value = it }
            carregarDados(psicologoApi::carregar) { _psicologos.value = it }
            carregarDados(agendaApi::carregar) { _agendas.value = it }
        }
    }

    private fun <T> carregarDados(
        carregar: suspend () -> List<T>,
        atualizar: (List<T>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                atualizar(carregar())
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar dados da evolução clínica"))
            }
        }
    }

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()
    private val _pacienteIdFiltro = MutableStateFlow<Long?>(null)
    val pacienteIdFiltro = _pacienteIdFiltro.asStateFlow()

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

    val backStack = NavBackStack<EvolucaoClinicaRotas>(
        EvolucaoClinicaRotas.Listagem
    )

    val id = mutableStateOf("")
    val prontuarioId = mutableStateOf<Long?>(null)
    val psicologoId = mutableStateOf<Long?>(null)
    val agendaId = mutableStateOf<Long?>(null)
    val dataRegistro = mutableStateOf("")
    val horaRegistro = mutableStateOf("")
    val descricao = mutableStateOf("")
    val ativo = mutableStateOf("true")

    fun salvar() {
        if (salvando) return

        val psicologoDaAgenda = _agendas.value
            .firstOrNull { it.id == agendaId.value }
            ?.psicologoId
            ?: psicologoId.value

        if (prontuarioId.value == null || psicologoDaAgenda == null || agendaId.value == null ||
            dataRegistro.value.isBlank() || descricao.value.isBlank()
        ) {
            mandarMensagem("Preencha os campos obrigatórios.")
            return
        }
        if (horaRegistro.value.isNotBlank() && !horaRegistro.value.matches(Regex("(?:[01]\\d|2[0-3]):[0-5]\\d"))) {
            mandarMensagem("Informe um horário válido no formato HH:mm.")
            return
        }

        val idAtual = id.value.toLongOrNull()
        if (id.value.isNotEmpty() && idAtual == null) {
            mandarMensagem("Identificador da evolução clínica inválido.")
            return
        }

        val ehNovo = idAtual == null
        val evolucao = EvolucaoClinica(
            id = idAtual,
            prontuarioId = prontuarioId.value!!,
            psicologoId = psicologoDaAgenda,
            agendaId = agendaId.value!!,
            dataRegistro = if (horaRegistro.value.isBlank()) {
                dataRegistro.value.trim()
            } else {
                "${formatarDataBrasileiraParaIso(dataRegistro.value.trim())}T${horaRegistro.value}:00"
            },
            descricao = descricao.value.trim(),
            ativo = ativo.value.toBooleanStrictOrNull() ?: true
        )

        viewModelScope.launch {
            salvando = true
            try {
                if (idAtual == null) {
                    evolucaoApi.salvar(evolucao)
                } else {
                    evolucaoApi.atualizar(idAtual, evolucao)
                }
                _evolucoes.value = evolucaoApi.carregar()

                val mensagemSucesso = if (ehNovo) {
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
            prontuarioId.value = null
            agendaId.value = null
            psicologoId.value = null
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
            prontuarioId.value = null
            agendaId.value = null
            psicologoId.value = null
        }
    }

    fun selecionarAgenda(agenda: Agenda) {
        agendaId.value = agenda.id
        psicologoId.value = agenda.psicologoId

        val prontuariosDoPaciente = _prontuarios.value.filter {
            it.pacienteId == agenda.pacienteId
        }
        val prontuarioAtual = prontuarioId.value?.takeIf { id ->
            prontuariosDoPaciente.any { it.id == id }
        }
        prontuarioId.value = prontuarioAtual ?: prontuariosDoPaciente.singleOrNull()?.id
    }

    fun selecionarProntuario(prontuario: Prontuario) {
        prontuarioId.value = prontuario.id
        val agendaAtual = _agendas.value.firstOrNull {
            it.id == agendaId.value && it.pacienteId == prontuario.pacienteId
        }
        agendaId.value = agendaAtual?.id
        psicologoId.value = agendaAtual?.psicologoId
    }

    fun editar(evolucao: EvolucaoClinica) {
        backStack.clear()
        backStack.add(EvolucaoClinicaRotas.Formulario)
        id.value = evolucao.id.toString()
        prontuarioId.value = evolucao.prontuarioId
        agendaId.value = evolucao.agendaId
        psicologoId.value = evolucao.psicologoId
        _pacienteIdFiltro.value = null
        _filtro.value = ""
        val partesDataHora = evolucao.dataRegistro.split('T', limit = 2)
        dataRegistro.value = formatarDataIsoParaBrasileira(partesDataHora[0])
        horaRegistro.value = partesDataHora.getOrNull(1)
            ?.take(5)
            ?.takeIf { it.matches(Regex("(?:[01]\\d|2[0-3]):[0-5]\\d")) }
            .orEmpty()
        descricao.value = evolucao.descricao.orEmpty()
        ativo.value = evolucao.ativo.toString()
    }

    fun limparCampos() {
        id.value = ""
        prontuarioId.value = null
        psicologoId.value = null
        agendaId.value = null
        dataRegistro.value = ""
        horaRegistro.value = ""
        descricao.value = ""
        ativo.value = "true"
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
            ?: agenda.paciente
            ?: "Paciente"
        return "$paciente - ${formatarDataHoraIsoParaBrasileira(agenda.dataHoraInicio)}"
    }
}