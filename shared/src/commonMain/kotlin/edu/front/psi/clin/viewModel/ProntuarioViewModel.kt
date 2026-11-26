package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.PacienteAPI
import edu.front.psi.clin.api.ProntuarioAPI
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.model.Prontuario
import edu.front.psi.clin.model.enums.ProntuarioStatus
import edu.front.psi.clin.navigation.ProntuarioRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.formatarDataBrasileiraParaIso
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProntuarioViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val prontuarioApi = ProntuarioAPI(httpClient)
    private val pacienteApi = PacienteAPI(httpClient)

    private val _prontuarios = MutableStateFlow<List<Prontuario>>(emptyList())
    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    val pacientes = _pacientes.asStateFlow()

    private var carregamentoDados: Job? = null

    var salvando by mutableStateOf(false)
        private set

    init {
        carregar()
    }

    fun carregar() {
        if (!PermissoesAcesso.podeAcessar(SessionManager.role, RecursoSistema.PRONTUARIOS)) return
        if (carregamentoDados?.isActive == true) return

        carregamentoDados = viewModelScope.launch {
            try {
                try {
                    _prontuarios.value = prontuarioApi.carregar()
                } catch (e: Exception) {
                    mandarMensagem(e.mensagemParaUsuario("Erro ao carregar prontuários"))
                }

                try {
                    _pacientes.value = pacienteApi.carregar()
                } catch (e: Exception) {
                    mandarMensagem(e.mensagemParaUsuario("Erro ao carregar pacientes"))
                }
            } finally {
                carregamentoDados = null
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

    fun selecionarPaciente(pacienteSelecionado: Paciente) {
        _pacienteIdFiltro.value = pacienteSelecionado.id
        _filtro.value = pacienteSelecionado.nome
        pacienteId = pacienteSelecionado.id
    }

    val prontuariosFiltrados = combine(
        _prontuarios,
        _pacientes,
        _filtro,
        _pacienteIdFiltro
    ) { prontuarios, pacientes, filtro, pacienteIdFiltro ->
        if (pacienteIdFiltro != null) {
            prontuarios.filter { it.pacienteId == pacienteIdFiltro }
        } else if (filtro.isEmpty()) {
            prontuarios
        } else {
            prontuarios.filter {
                pacientes.firstOrNull { paciente -> paciente.id == it.pacienteId }
                    ?.nome?.contains(filtro, ignoreCase = true) == true ||
                    it.queixaPrincipal.contains(filtro, ignoreCase = true) ||
                    it.status.rotulo.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<ProntuarioRotas>(
        ProntuarioRotas.Listagem
    )

    val id = mutableStateOf("")

    var pacienteId by mutableStateOf<Long?>(null)
        private set

    val dataAbertura = CampoValidado()
    val queixaPrincipal = CampoValidado()
    val status = CampoValidado(ProntuarioStatus.ATIVO.name)
    val ativo = CampoValidado("true")

    fun salvar() {
        if (salvando) return

        val pacienteSelecionadoId = pacienteId ?: _pacienteIdFiltro.value
        val v2 = dataAbertura.validar()
        val v3 = queixaPrincipal.validar()
        val v4 = status.validar()
        val v5 = ativo.validar()

        if (pacienteSelecionadoId == null) {
            mandarMensagem("Nenhum paciente selecionado para o prontuário.")
            return
        }

        if (!(v2 && v3 && v4 && v5)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        val dataFormatada = formatarDataBrasileiraParaIso(dataAbertura.valor.value.trim())

        viewModelScope.launch {
            salvando = true
            try {
                val prontuario = Prontuario(
                    id = id.value.toLongOrNull(),
                    pacienteId = pacienteSelecionadoId,
                    dataAbertura = dataFormatada,
                    queixaPrincipal = queixaPrincipal.valor.value.trim(),
                    status = ProntuarioStatus.deValorApi(status.valor.value),
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    prontuarioApi.salvar(prontuario)
                } else {
                    val idNumber = id.value.toLong()
                    prontuarioApi.atualizar(idNumber, prontuario)
                }

                _prontuarios.value = prontuarioApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Prontuário salvo com sucesso!"
                } else {
                    "Prontuário atualizado com sucesso!"
                }

                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(ProntuarioRotas.Listagem)
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar prontuário"))
            } finally {
                salvando = false
            }
        }
    }

    fun apagar(prontuario: Prontuario) {
        viewModelScope.launch {
            try {
                prontuarioApi.apagar(prontuario)
                _prontuarios.value = prontuarioApi.carregar()
                mandarMensagem("Prontuário apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar prontuário"))
            }
        }
    }

    fun novoProntuario(nomePaciente: String = "") {
        backStack.clear()
        backStack.add(ProntuarioRotas.Formulario)
        id.value = ""
        limparCampos()

        if (pacienteId == null && nomePaciente.isNotEmpty() && nomePaciente != "Todos os Pacientes") {
            pacienteId = _pacientes.value.firstOrNull {
                it.nome.equals(nomePaciente.trim(), ignoreCase = true)
            }?.id
        }
    }

    fun editar(prontuario: Prontuario) {
        backStack.clear()
        backStack.add(ProntuarioRotas.Formulario)
        id.value = prontuario.id?.toString() ?: ""
        pacienteId = prontuario.pacienteId
        dataAbertura.definir(formatarDataIsoParaBrasileira(prontuario.dataAbertura))
        queixaPrincipal.definir(prontuario.queixaPrincipal)
        status.definir(prontuario.status.name)
        ativo.definir(prontuario.ativo.toString())
    }

    fun limparCampos() {
        pacienteId = _pacienteIdFiltro.value
        dataAbertura.limpar()
        queixaPrincipal.limpar()
        status.definir(ProntuarioStatus.ATIVO.name)
        ativo.definir("true")
    }
}