package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.PacienteAPI
import edu.front.psi.clin.api.ProntuarioAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.Prontuario
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.navigation.ProntuarioRotas
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
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

    init {
        viewModelScope.launch {
            try {
                _prontuarios.value = prontuarioApi.carregar()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar prontuários"))
            }
        }
        viewModelScope.launch {
            try {
                _pacientes.value = pacienteApi.carregar()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar pacientes"))
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
        paciente.value = pacienteSelecionado.nome
        _filtro.value = pacienteSelecionado.nome
        _pacienteIdFiltro.value = pacienteSelecionado.id
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
                it.queixaPrincipal.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<ProntuarioRotas>(
        ProntuarioRotas.Listagem
    )


    val id = mutableStateOf("")
    val paciente = mutableStateOf("")
    val dataAbertura = mutableStateOf("")
    val queixaPrincipal = mutableStateOf("")
    val status = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        if (paciente.value.isBlank() || queixaPrincipal.value.isBlank()) {
            mensagem.value = "Preencha os campos obrigatórios!"
            mandarMensagem("Preencha os campos obrigatórios!")
            return
        }
        val pacienteId = _pacienteIdFiltro.value
            ?: _pacientes.value.firstOrNull { it.nome == paciente.value }?.id
        if (pacienteId == null) {
            mensagem.value = "Selecione um paciente cadastrado."
            mandarMensagem(mensagem.value)
            return
        }

        val idAtual = id.value.toLongOrNull()
        if (id.value.isNotEmpty() && idAtual == null) {
            mensagem.value = "Identificador do prontuário inválido."
            mandarMensagem("Identificador do prontuário inválido.")
            return
        }

        val ehNovo = idAtual == null
        val statusApi = when (status.value.trim().lowercase()) {
            "arquivado" -> "ARQUIVADO"
            "encerrado", "false", "0", "inativo", "não", "nao" -> "ENCERRADO"
            else -> "ATIVO"
        }
        val prontuario = Prontuario(
            id = idAtual,
            pacienteId = pacienteId,
            dataAbertura = dataAbertura.value.trim().ifEmpty { null },
            queixaPrincipal = queixaPrincipal.value.trim(),
            status = statusApi,
            ativo = statusApi == "ATIVO"
        )

        viewModelScope.launch {
            try {
                if (idAtual == null) {
                    prontuarioApi.salvar(prontuario)
                } else {
                    prontuarioApi.atualizar(idAtual, prontuario)
                }
                _prontuarios.value = prontuarioApi.carregar()

                val mensagemSucesso = if (ehNovo) {
                    "Prontuário salvo com sucesso!"
                } else {
                    "Prontuário atualizado com sucesso!"
                }
                mensagem.value = mensagemSucesso
                mandarMensagem(mensagemSucesso)
                limparCampos()
                backStack.clear()
                backStack.add(ProntuarioRotas.Listagem)
            } catch (e: Exception) {
                val mensagemErro = e.mensagemParaUsuario("Erro ao salvar prontuário")
                mensagem.value = mensagemErro
                mandarMensagem(mensagemErro)
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
        limparCampos()
        if (nomePaciente.isNotEmpty()) {
            paciente.value = nomePaciente
        }
    }

    fun editar(prontuario: Prontuario) {
        backStack.clear()
        backStack.add(ProntuarioRotas.Formulario)
        id.value = prontuario.id?.toString() ?: ""
        paciente.value = _pacientes.value.firstOrNull { it.id == prontuario.pacienteId }?.nome.orEmpty()
        dataAbertura.value = prontuario.dataAbertura?.let(::formatarDataIsoParaBrasileira).orEmpty()
        queixaPrincipal.value = prontuario.queixaPrincipal
        status.value = when (prontuario.status?.uppercase()) {
            "ARQUIVADO" -> "Arquivado"
            "ENCERRADO" -> "Encerrado"
            else -> "Ativo"
        }
        ativo.value = prontuario.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        id.value = ""
        paciente.value = ""
        dataAbertura.value = ""
        queixaPrincipal.value = ""
        status.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}