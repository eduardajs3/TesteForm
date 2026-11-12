package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.RecepcionistaAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.Recepcionista
import edu.front.psi.clin.navigation.RecepcionistaRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecepcionistaViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val recepcionistaApi = RecepcionistaAPI(httpClient)

    private val _recepcionistas = MutableStateFlow<List<Recepcionista>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _recepcionistas.value = recepcionistaApi.carregar()
                println("Recepcionistas carregadas no init: ${_recepcionistas.value}")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar recepcionistas do servidor: ${e.message}")
            }
        }
    }

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

    val backStack = NavBackStack<RecepcionistaRotas>(
        RecepcionistaRotas.Listagem
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
    val setor = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios
        val v1 = nome.validar()
        val v2 = cpf.validar()
        val v3 = email.validar()
        val v4 = telefone.validar()
        val v5 = senha.validar()
        val v6 = ativo.validar()
        val v7 = setor.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val recepcionista = Recepcionista(
                    id = null, 
                    nome = nome.valor.value,
                    cpf = cpf.valor.value,
                    email = email.valor.value,
                    telefone = telefone.valor.value,
                    senha = senha.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    dataCadastro = null, 
                    setor = setor.valor.value,
                    role = "RECEPCIONISTA" 
                )

                if (id.value.isEmpty()) {
                    recepcionistaApi.salvar(recepcionista)
                } else {
                    val idNumber = id.value.toLong()
                    recepcionistaApi.atualizar(idNumber, recepcionista)
                }

                // Recarrega a lista direto da API
                _recepcionistas.value = recepcionistaApi.carregar()
                println("Recepcionistas após salvar: ${_recepcionistas.value}")

                mensagem.value = "Recepcionista salva com sucesso!"
                mandarMensagem("Recepcionista salva com sucesso!")
                limparCampos()
                
                backStack.clear()
                backStack.add(RecepcionistaRotas.Listagem)

            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(recepcionista: Recepcionista) {
        viewModelScope.launch {
            try {
                recepcionistaApi.apagar(recepcionista)
                _recepcionistas.value = recepcionistaApi.carregar()
                println("Recepcionistas após apagar: ${_recepcionistas.value}")
                mandarMensagem("Recepcionista apagada com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novaRecepcionista() {
        backStack.clear()
        backStack.add(RecepcionistaRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(recepcionista: Recepcionista) {
        backStack.clear()
        backStack.add(RecepcionistaRotas.Formulario)
        id.value = recepcionista.id.toString()
        nome.definir(recepcionista.nome)
        cpf.definir(recepcionista.cpf)
        email.definir(recepcionista.email)
        telefone.definir(recepcionista.telefone)
        senha.definir(recepcionista.senha ?: "") 
        ativo.definir(recepcionista.ativo.toString())
        dataCadastro.definir(recepcionista.dataCadastro ?: "")
        setor.definir(recepcionista.setor)
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
        setor.limpar()
        mensagem.value = "Campos limpos."
    }
}