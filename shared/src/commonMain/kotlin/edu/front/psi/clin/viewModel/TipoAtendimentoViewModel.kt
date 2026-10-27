package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.TipoAtendimento // Ajuste para o pacote correto do seu Model
import kotlinx.coroutines.flow.*

class TipoAtendimentoViewModel : ViewModel() {

    private var idCounter: Int = 0
    private val _tiposAtendimento = MutableStateFlow<List<TipoAtendimento>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val tiposFiltrados = combine(_tiposAtendimento, _filtro) { tipos, filtro ->
        if (filtro.isEmpty()) {
            tipos
        } else {
            tipos.filter { it.nome.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    val id = mutableStateOf("")
    val nome = mutableStateOf("")
    val descricao = mutableStateOf("")
    val duracao = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val dataCadastro = mutableStateOf("")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val tipo = TipoAtendimento(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.value,
                descricao = descricao.value,
                duracao = duracao.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.value
            )

            if (id.value.isEmpty()) {
                idCounter++
                _tiposAtendimento.update { lista -> lista + tipo.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _tiposAtendimento.update { lista ->
                    lista.map { t -> if (t.id == idNumber) tipo.copy(id = idNumber) else t }
                }
            }
            mensagem.value = "Tipo de atendimento salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(tipo: TipoAtendimento) {
        _tiposAtendimento.update { lista -> lista - tipo }
    }

    fun novoTipo() {
        tela.value = "form"
        id.value = ""
        nome.value = ""
        descricao.value = ""
        duracao.value = ""
        ativo.value = "true"
        dataCadastro.value = ""
        mensagem.value = ""
    }

    fun editar(tipo: TipoAtendimento) {
        tela.value = "form"
        id.value = tipo.id.toString()
        nome.value = tipo.nome
        descricao.value = tipo.descricao
        duracao.value = tipo.duracao
        ativo.value = tipo.ativo.toString()
        dataCadastro.value = tipo.dataCadastro
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.value = ""
        descricao.value = ""
        duracao.value = ""
        ativo.value = "true"
        dataCadastro.value = ""
        mensagem.value = "Campos limpos."
    }
}