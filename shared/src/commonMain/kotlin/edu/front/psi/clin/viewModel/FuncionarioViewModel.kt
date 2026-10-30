package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Funcionario
import edu.front.psi.clin.ui.components.CampoValidado
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

    // Campos do formulário geridos pelo validador
    val id = mutableStateOf("")
    val nome = CampoValidado()
    val cpf = CampoValidado()
    val email = CampoValidado()
    val telefone = CampoValidado()
    val senha = CampoValidado()
    val ativo = CampoValidado("true")
    val dataCadastro = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios
        val v1 = nome.validar()
        val v2 = cpf.validar()
        val v3 = email.validar()
        val v4 = telefone.validar()
        val v5 = senha.validar()
        val v6 = ativo.validar()
        val v7 = dataCadastro.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            return
        }

        try {
            val funcionario = Funcionario(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.valor.value,
                cpf = cpf.valor.value,
                email = email.valor.value,
                telefone = telefone.valor.value,
                senha = senha.valor.value,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.valor.value
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
            limparCampos()
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
        limparCampos()
        mensagem.value = ""
    }

    fun editar(funcionario: Funcionario) {
        tela.value = "form"
        id.value = funcionario.id.toString()
        nome.definir(funcionario.nome)
        cpf.definir(funcionario.cpf)
        email.definir(funcionario.email)
        telefone.definir(funcionario.telefone)
        senha.definir(funcionario.senha)
        ativo.definir(funcionario.ativo.toString())
        dataCadastro.definir(funcionario.dataCadastro)
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.limpar()
        cpf.limpar()
        email.limpar()
        telefone.limpar()
        senha.limpar()
        ativo.definir("true")
        dataCadastro.limpar()
        mensagem.value = "Campos limpos."
    }
}