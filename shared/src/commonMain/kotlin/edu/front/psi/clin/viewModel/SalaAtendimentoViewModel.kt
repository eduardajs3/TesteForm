package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.SalaAtendimentoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.SalaAtendimento
import edu.front.psi.clin.navigation.SalaAtendimentoRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SalaAtendimentoViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val salaApi = SalaAtendimentoAPI(httpClient)

    private val _salas = MutableStateFlow<List<SalaAtendimento>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _salas.value = salaApi.carregar()
                println("Salas carregadas no init: ${_salas.value}")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar salas do servidor: ${e.message}")
            }
        }
    }

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

    val backStack = NavBackStack<SalaAtendimentoRotas>(
        SalaAtendimentoRotas.Listagem
    )

    val id = mutableStateOf("")
    val nome = CampoValidado()
    val descricao = CampoValidado()
    val capacidade = CampoValidado()
    val ativo = CampoValidado("true")
    val dataCadastro = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
     
        val v1 = nome.validar()
        val v2 = descricao.validar()
        val v3 = capacidade.validar()
        val v4 = ativo.validar()
        val v5 = dataCadastro.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val sala = SalaAtendimento(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    descricao = descricao.valor.value,
                    capacidade = capacidade.valor.value.toIntOrNull() ?: 0,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    dataCadastro = dataCadastro.valor.value
                )

                if (id.value.isEmpty()) {
                    salaApi.salvar(sala)
                } else {
                    val idNumber = id.value.toLong()
                    salaApi.atualizar(idNumber, sala)
                }

                _salas.value = salaApi.carregar()
                println("Salas após salvar: ${_salas.value}")

                mensagem.value = "Sala salva com sucesso!"
                mandarMensagem("Sala salva com sucesso!")
                limparCampos()
                
                backStack.clear()
                backStack.add(SalaAtendimentoRotas.Listagem)

            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(sala: SalaAtendimento) {
        viewModelScope.launch {
            try {
                salaApi.apagar(sala)
                _salas.value = salaApi.carregar()
                println("Salas após apagar: ${_salas.value}")
                mandarMensagem("Sala apagada com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novaSala() {
        backStack.clear()
        backStack.add(SalaAtendimentoRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(sala: SalaAtendimento) {
        backStack.clear()
        backStack.add(SalaAtendimentoRotas.Formulario)
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