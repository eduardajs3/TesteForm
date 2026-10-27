package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Funcionario
import kotlinx.coroutines.flow.*

class FuncionarioViewModel : ViewModel() {

    private var idCounter: Int = 2

    private val _funcionarios = MutableStateFlow<List<Funcionario>>(
        listOf(
            Funcionario(
                id = 1,
                nome = "Carlos Eduardo",
                cpf = "321.654.987-00",
                email = "carlos@email.com",
                telefone = "(11) 96666-5555",
                senha = "123",
                ativo = true,
                dataCadastro = "15/01/2024"
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val funcionariosFiltrados = combine(_funcionarios, _filtro) { funcionarios, filtro ->
        if (filtro.isEmpty()) {
            funcionarios
        } else {
            funcionarios.filter {
                it.nome.contains(filtro, ignoreCase = true) ||
                it.cpf.contains(filtro, ignoreCase = true) ||
                it.email.contains(filtro, ignoreCase = true)
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
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val funcionario = Funcionario(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.value,
                cpf = cpf.value,
                email = email.value,
                telefone = telefone.value,
                senha = senha.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.value
            )

            if (id.value.isEmpty()) {
                idCounter++
                _funcionarios.update { lista -> lista + funcionario.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _funcionarios.update { lista ->
                    lista.map { item -> if (item.id == idNumber) funcionario.copy(id = idNumber) else item }
                }
            }
            mensagem.value = "Funcionário salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(funcionario: Funcionario) {
        _funcionarios.update { lista -> lista - funcionario }
    }

    fun novoFuncionario() {
        tela.value = "form"
        id.value = ""
        nome.value = ""
        cpf.value = ""
        email.value = ""
        telefone.value = ""
        senha.value = ""
        ativo.value = "true"
        dataCadastro.value = ""
        mensagem.value = ""
    }

    fun editar(funcionario: Funcionario) {
        tela.value = "form"
        id.value = funcionario.id.toString()
        nome.value = funcionario.nome
        cpf.value = funcionario.cpf
        email.value = funcionario.email
        telefone.value = funcionario.telefone
        senha.value = funcionario.senha
        ativo.value = funcionario.ativo.toString()
        dataCadastro.value = funcionario.dataCadastro
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
        mensagem.value = "Campos limpos."
    }
}