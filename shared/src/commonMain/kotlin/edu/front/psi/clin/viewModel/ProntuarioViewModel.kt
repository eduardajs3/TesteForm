package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.ProntuarioAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.Prontuario
import edu.front.psi.clin.navigation.ProntuarioRotas
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProntuarioViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val prontuarioApi = ProntuarioAPI(httpClientManager())
    private val _prontuarios = MutableStateFlow<List<Prontuario>>(emptyList())

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        recarregar()
    }

    fun recarregar() {
        viewModelScope.launch {
            try {
                _prontuarios.value = prontuarioApi.carregar()
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar prontuários do servidor: ${e.message}")
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro.trim()
    }

    val prontuariosFiltrados = combine(_prontuarios, _filtro) { prontuarios, filtro ->
        if (filtro.isEmpty()) {
            prontuarios
        } else {
            prontuarios.filter {
                it.paciente.contains(filtro, ignoreCase = true) ||
                it.queixaPrincipal.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<ProntuarioRotas>(
        ProntuarioRotas.Listagem
    )

    // Campos do formulário
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

        viewModelScope.launch {
            try {
                val prontuarioItem = Prontuario(
                    id = id.value.toIntOrNull() ?: 0,
                    paciente = paciente.value.trim(),
                    dataAbertura = dataAbertura.value.trim(),
                    queixaPrincipal = queixaPrincipal.value.trim(),
                    status = status.value.trim().ifEmpty { "Em Acompanhamento" },
                    ativo = ativo.value.trim().lowercase() !in listOf("false", "0", "inativo", "não", "nao")
                )

                if (id.value.isEmpty()) {
                    prontuarioApi.salvar(prontuarioItem)
                } else {
                    prontuarioApi.atualizar(id.value.toLong(), prontuarioItem)
                }
                _prontuarios.value = prontuarioApi.carregar()

                _filtro.value = ""
                mensagem.value = "Prontuário salvo com sucesso!"
                mandarMensagem("Prontuário salvo com sucesso!")
                limparCampos()
                backStack.clear()
                backStack.add(ProntuarioRotas.Listagem)
            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
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
                mandarMensagem("Erro ao apagar prontuário: ${e.message}")
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
        id.value = prontuario.id.toString()
        paciente.value = prontuario.paciente
        dataAbertura.value = prontuario.dataAbertura
        queixaPrincipal.value = prontuario.queixaPrincipal
        status.value = prontuario.status
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