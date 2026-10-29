package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Prontuario
import kotlinx.coroutines.flow.*

class ProntuarioViewModel : ViewModel() {

    private var idCounter: Int = 1

    private val _prontuarios = MutableStateFlow<List<Prontuario>>(
        listOf(
            Prontuario(
                id = 1,
                paciente = "Maria Clara da Silva",
                dataAbertura = "10/10/2023",
                queixaPrincipal = "Ansiedade social e insônia",
                status = "Em Acompanhamento",
                ativo = true
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

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

    // Estado de navegação interno ("list" ou "form")
    val tela = mutableStateOf("list")

    // Campos do formulário
    val id = mutableStateOf("")
    val paciente = mutableStateOf("")
    val dataAbertura = mutableStateOf("")
    val queixaPrincipal = mutableStateOf("")
    val status = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            if (paciente.value.isBlank() || queixaPrincipal.value.isBlank()) {
                mensagem.value = "Preencha os campos obrigatórios!"
                return
            }

            val ehNovo = id.value.isEmpty()
            val novoId = if (ehNovo) ++idCounter else id.value.toInt()
            
            // Tratamento permissivo para o campo booleano
            val statusBooleano = when (ativo.value.trim().lowercase()) {
                "false", "0", "inativo", "não", "nao" -> false
                else -> true
            }

            val prontuarioItem = Prontuario(
                id = novoId,
                paciente = paciente.value.trim(),
                dataAbertura = dataAbertura.value.trim(),
                queixaPrincipal = queixaPrincipal.value.trim(),
                status = status.value.trim().ifEmpty { "Em Acompanhamento" },
                ativo = statusBooleano
            )

            if (ehNovo) {
                _prontuarios.update { lista -> lista + prontuarioItem }
            } else {
                _prontuarios.update { lista ->
                    lista.map { item -> if (item.id == novoId) prontuarioItem else item }
                }
            }

            // Limpa o filtro de busca para garantir que o novo prontuário apareça na lista
            _filtro.value = ""
            
            limparCampos()
            mensagem.value = "Prontuário salvo com sucesso!"
            tela.value = "list" // Retorna à listagem
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(prontuario: Prontuario) {
        _prontuarios.update { lista -> lista.filterNot { it.id == prontuario.id } }
    }

    fun novoProntuario(nomePaciente: String = "") {
        limparCampos()
        if (nomePaciente.isNotEmpty()) {
            paciente.value = nomePaciente
        }
        tela.value = "form"
    }

    fun editar(prontuario: Prontuario) {
        id.value = prontuario.id.toString()
        paciente.value = prontuario.paciente
        dataAbertura.value = prontuario.dataAbertura
        queixaPrincipal.value = prontuario.queixaPrincipal
        status.value = prontuario.status
        ativo.value = prontuario.ativo.toString()
        mensagem.value = ""
        tela.value = "form"
    }

    fun limparCampos() {
        id.value = ""
        paciente.value = ""
        dataAbertura.value = ""
        queixaPrincipal.value = ""
        status.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }
}