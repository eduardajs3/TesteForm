package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.SalaAtendimento
import edu.front.psi.clin.ui.components.CampoValidado
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

    // Campos do formulário geridos pelo validador
    val id = mutableStateOf("")
    val nome = CampoValidado()
    val descricao = CampoValidado()
    val capacidade = CampoValidado()
    val ativo = CampoValidado("true")
    val dataCadastro = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios
        val v1 = nome.validar()
        val v2 = descricao.validar()
        val v3 = capacidade.validar()
        val v4 = ativo.validar()
        val v5 = dataCadastro.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            return
        }

        try {
            val sala = SalaAtendimento(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.valor.value,
                descricao = descricao.valor.value,
                capacidade = capacidade.valor.value.toIntOrNull() ?: 0,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.valor.value
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
            limparCampos()
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
        limparCampos()
        mensagem.value = ""
    }

    fun editar(sala: SalaAtendimento) {
        tela.value = "form"
        id.value = sala.id.toString()
        nome.definir(sala.nome)
        descricao.definir(sala.descricao)
        capacidade.definir(sala.capacidade.toString())
        ativo.definir(sala.ativo.toString())
        dataCadastro.definir(sala.dataCadastro)
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.limpar()
        descricao.limpar()
        capacidade.limpar()
        ativo.definir("true")
        dataCadastro.limpar()
        mensagem.value = "Campos limpos."
    }
}