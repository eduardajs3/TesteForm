package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Prontuario
import kotlinx.coroutines.flow.*

class ProntuarioViewModel : ViewModel() {

    private var idCounter: Int = 2

    private val _prontuarios = MutableStateFlow<List<Prontuario>>(
        listOf(
            Prontuario(
                id = 1,
                paciente = "João Silva",
                dataAbertura = "10/10/2023",
                queixaPrincipal = "Ansiedade e insônia",
                status = "Em Aberto",
                ativo = true
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
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

    val tela = mutableStateOf("list") // "list" ou "form"

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
            val prontuario = Prontuario(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                paciente = paciente.value,
                dataAbertura = dataAbertura.value,
                queixaPrincipal = queixaPrincipal.value,
                status = status.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _prontuarios.update { lista -> lista + prontuario.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _prontuarios.update { lista ->
                    lista.map { item -> if (item.id == idNumber) prontuario.copy(id = idNumber) else item }
                }
            }
            mensagem.value = "Prontuário salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(prontuario: Prontuario) {
        _prontuarios.update { lista -> lista - prontuario }
    }

    fun novoProntuario() {
        tela.value = "form"
        id.value = ""
        paciente.value = ""
        dataAbertura.value = ""
        queixaPrincipal.value = ""
        status.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun editar(prontuario: Prontuario) {
        tela.value = "form"
        id.value = prontuario.id.toString()
        paciente.value = prontuario.paciente
        dataAbertura.value = prontuario.dataAbertura
        queixaPrincipal.value = prontuario.queixaPrincipal
        status.value = prontuario.status
        ativo.value = prontuario.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        paciente.value = ""
        dataAbertura.value = ""
        queixaPrincipal.value = ""
        status.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}