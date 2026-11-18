package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.FuncionarioAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
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

    var salvando by mutableStateOf(false)
        private set

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

    init {
        viewModelScope.launch {
            try {
                _funcionarios.value = funcionarioApi.carregar()
                println("Funcionários carregados: ${_funcionarios.value}")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar funcionários do servidor"))
            }
        }
    }

    fun salvar() {
        if (salvando) return

        val v1 = nome.validar()
        val v2 = cpf.validar()
        val v3 = email.validar()
        val v4 = telefone.validar()
        val v5 = id.value.isNotEmpty() || senha.validar()
        val v6 = ativo.validar()
        val senhaNova = senha.valor.value.takeIf { it.isNotBlank() }

        if (!(v1 && v2 && v3 && v4 && v5 && v6)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val funcionario = Funcionario(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    cpf = cpf.valor.value,
                    email = email.valor.value,
                    telefone = telefone.valor.value,
                    senha = senha.valor.value.takeIf { it.isNotBlank() },
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    role = "ADMIN"
                )

                if (id.value.isEmpty()) {
                    funcionarioApi.salvar(funcionario)
                } else {
                    val idNumber = id.value.toLong()
                    funcionarioApi.atualizar(idNumber, funcionario)
                    if (senhaNova != null) {
                        try {
                            funcionarioApi.alterarSenha(idNumber, senhaNova)
                        } catch (e: Exception) {
                            mandarMensagem(
                                e.mensagemParaUsuario("Dados atualizados, mas não foi possível alterar a senha")
                            )
                            return@launch
                        }
                    }
                }

                _funcionarios.value = funcionarioApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Funcionário salvo com sucesso!"
                } else {
                    "Funcionário atualizado com sucesso!"
                }
                
                mandarMensagem(mensagemSucesso)
                limparCampos()
                backStack.clear()
                backStack.add(FuncionarioRotas.Listagem)
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar"))
            } finally {
                salvando = false
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
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar"))
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