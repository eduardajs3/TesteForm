package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Recepcionista
import kotlinx.coroutines.flow.*

class RecepcionistaViewModel : ViewModel() {

    private var idCounter: Int = 2

    private val _recepcionistas = MutableStateFlow<List<Recepcionista>>(
        listOf(
            Recepcionista(
                id = 1,
                nome = "Maria Oliveira",
                cpf = "123.456.789-00",
                email = "maria@email.com",
                telefone = "(11) 98888-7777",
                senha = "123",
                ativo = true,
                dataCadastro = "01/02/2024",
                setor = "Recepção Principal"
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val recepcionistasFiltrados = combine(_recepcionistas, _filtro) { recepcionistas, filtro ->
        if (filtro.isEmpty()) {
            recepcionistas
        } else {
            recepcionistas.filter {
                it.nome.contains(filtro, ignoreCase = true) ||
                it.cpf.contains(filtro, ignoreCase = true) ||
                it.setor.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    // Campos do formulário
    val id = mutableStateOf("")
    val nome = mutableStateOf("")
    val cpf = mutableStateOf("")
    val email = mutableStateOf("")
    val telefone = mutableStateOf("")
    val senha = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val dataCadastro = mutableStateOf("")
    val setor = mutableStateOf("")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val recepcionista = Recepcionista(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.value,
                cpf = cpf.value,
                email = email.value,
                telefone = telefone.value,
                senha = senha.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.value,
                setor = setor.value
            )

            if (id.value.isEmpty()) {
                idCounter++
                _recepcionistas.update { lista -> lista + recepcionista.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _recepcionistas.update { lista ->
                    lista.map { item -> if (item.id == idNumber) recepcionista.copy(id = idNumber) else item }
                }
            }
            mensagem.value = "Recepcionista salva com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(recepcionista: Recepcionista) {
        _recepcionistas.update { lista -> lista - recepcionista }
    }

    fun novaRecepcionista() {
        tela.value = "form"
        id.value = ""
        nome.value = ""
        cpf.value = ""
        email.value = ""
        telefone.value = ""
        senha.value = ""
        ativo.value = "true"
        dataCadastro.value = ""
        setor.value = ""
        mensagem.value = ""
    }

    fun editar(recepcionista: Recepcionista) {
        tela.value = "form"
        id.value = recepcionista.id.toString()
        nome.value = recepcionista.nome
        cpf.value = recepcionista.cpf
        email.value = recepcionista.email
        telefone.value = recepcionista.telefone
        senha.value = recepcionista.senha
        ativo.value = recepcionista.ativo.toString()
        dataCadastro.value = recepcionista.dataCadastro
        setor.value = recepcionista.setor
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.value = ""
        cpf.value = ""
        email.value = ""
        telefone.value = ""
        senha.value = ""
        ativo.value = "true"
        dataCadastro.value = ""
        setor.value = ""
        mensagem.value = "Campos limpos."
    }
}