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

    private val httpClient = httpClientManager()
    private val funcionarioApi = FuncionarioAPI(httpClient)
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
                it.nome.orEmpty().contains(filtro, ignoreCase = true) ||
                it.cpf.orEmpty().contains(filtro, ignoreCase = true) ||
                it.email.orEmpty().contains(filtro, ignoreCase = true)
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
    val mensagem = mutableStateOf("")

    init {
        viewModelScope.launch {
            try {
                _funcionarios.value = funcionarioApi.carregar()
                println("Funcionários carregados: ${_funcionarios.value}")
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

        if (!(v1 && v2 && v3 && v4 && v5 && v6)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val funcionario = Funcionario(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    cpf = cpf.valor.value,
                    email = email.valor.value,
                    telefone = telefone.valor.value,
                    senha = senha.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    role = "ADMIN"
                )

                if (id.value.isEmpty()) {
                    funcionarioApi.salvar(funcionario)
                } else {
                    funcionarioApi.atualizar(id.value.toLong(), funcionario)
                }

                _funcionarios.value = funcionarioApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Funcionário salvo com sucesso!"
                } else {
                    "Funcionário atualizado com sucesso!"
                }
                mensagem.value = mensagemSucesso
                mandarMensagem(mensagemSucesso)
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
                println("Funcionários após apagar: ${_funcionarios.value}")
                mandarMensagem("Funcionário apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
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
        id.value = funcionario.id?.toString() ?: ""
        nome.definir(funcionario.nome.orEmpty())
        cpf.definir(funcionario.cpf.orEmpty())
        email.definir(funcionario.email.orEmpty())
        telefone.definir(funcionario.telefone.orEmpty())
        senha.definir("")
        ativo.definir((funcionario.ativo ?: true).toString())
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.limpar()
        cpf.limpar()
        email.limpar()
        telefone.limpar()
        senha.limpar()
        ativo.definir("true")
    }
}