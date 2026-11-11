package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.FuncionarioAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.Funcionario
import edu.front.psi.clin.navigation.FuncionarioRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FuncionarioViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val funcionarioApi = FuncionarioAPI(httpClientManager())
    private val _funcionarios = MutableStateFlow<List<Funcionario>>(emptyList())

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

    val backStack =
        NavBackStack<FuncionarioRotas>(
            FuncionarioRotas.Listagem
        )

    val id = mutableStateOf("")
    val nome = CampoValidado()
    val cpf = CampoValidado(formatador = { MaskUtils.aplicarMascaraCpf(it) })
    val email = CampoValidado(
        validadorExtra = { MaskUtils.validarEmail(it) },
        mensagemErroPersonalizada = "E-mail inválido"
    )
    val telefone = CampoValidado(formatador = { MaskUtils.aplicarMascaraTelefone(it) })
    val senha = CampoValidado()
    val ativo = CampoValidado("true")
    val dataCadastro = CampoValidado()
    
    val mensagem = mutableStateOf("")

    init {
        recarregar()
    }

    fun recarregar() {
        viewModelScope.launch {
            try {
                _funcionarios.value = funcionarioApi.carregar()
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar funcionários do servidor: ${e.message}")
            }
        }
    }

    fun salvar() {
        val v1 = nome.validar()
        val v2 = cpf.validar()
        val v3 = email.validar()
        val v4 = telefone.validar()
        val v5 = senha.validar()
        val v6 = ativo.validar()
        val v7 = dataCadastro.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val funcionario = Funcionario(
                    id = id.value.toIntOrNull() ?: 0,
                    nome = nome.valor.value,
                    cpf = cpf.valor.value,
                    email = email.valor.value,
                    telefone = telefone.valor.value,
                    senha = senha.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    dataCadastro = dataCadastro.valor.value
                )

                if (id.value.isEmpty()) {
                    funcionarioApi.salvar(funcionario)
                } else {
                    funcionarioApi.atualizar(id.value.toLong(), funcionario)
                }
                _funcionarios.value = funcionarioApi.carregar()

                mensagem.value = "Funcionário salvo com sucesso!"
                mandarMensagem("Funcionário salvo com sucesso!")
                limparCampos()
                backStack.clear()
                backStack.add(FuncionarioRotas.Listagem)
            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(funcionario: Funcionario) {
        viewModelScope.launch {
            try {
                funcionarioApi.apagar(funcionario)
                _funcionarios.value = funcionarioApi.carregar()
                mandarMensagem("Funcionário apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar funcionário: ${e.message}")
            }
        }
    }

    fun novoFuncionario() {
        backStack.clear()
        backStack.add(
            FuncionarioRotas.Formulario
        )
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(funcionario: Funcionario) {
        backStack.clear()
        backStack.add(
            FuncionarioRotas.Formulario
        )
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
    }
}