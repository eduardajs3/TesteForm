package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.SalaAtendimento // Certifique-se de importar o seu Model de Sala
import kotlinx.coroutines.flow.*

class SalaAtendimentoViewModel : ViewModel() {

    private var idCounter: Int = 0

    private val _salas = MutableStateFlow<List<SalaAtendimento>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val salasFiltradas = combine(_salas, _filtro) { salas, filtro ->
        if (filtro.isEmpty()) {
            salas
        } else {
            salas.filter { it.nome.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    // Campos do formulário
    val id = mutableStateOf("")
    val nome = mutableStateOf("")
    val descricao = mutableStateOf("")
    val capacidade = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val dataCadastro = mutableStateOf("")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val sala = SalaAtendimento(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.value,
                descricao = descricao.value,
                capacidade = capacidade.value.toIntOrNull() ?: 0,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.value
            )

            if (id.value.isEmpty()) {
                idCounter++
                _salas.update { lista -> lista + sala.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _salas.update { lista ->
                    lista.map { s -> if (s.id == idNumber) sala.copy(id = idNumber) else s }
                }
            }
            mensagem.value = "Sala salva com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(sala: SalaAtendimento) {
        _salas.update { lista -> lista - sala }
    }

    fun novaSala() {
        tela.value = "form"
        id.value = ""
        nome.value = ""
        descricao.value = ""
        capacidade.value = ""
        ativo.value = "true"
        dataCadastro.value = ""
        mensagem.value = ""
    }

    fun editar(sala: SalaAtendimento) {
        tela.value = "form"
        id.value = sala.id.toString()
        nome.value = sala.nome
        descricao.value = sala.descricao
        capacidade.value = sala.capacidade.toString()
        ativo.value = sala.ativo.toString()
        dataCadastro.value = sala.dataCadastro
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.value = ""
        descricao.value = ""
        capacidade.value = ""
        ativo.value = "true"
        dataCadastro.value = ""
        mensagem.value = "Campos limpos."
    }
}