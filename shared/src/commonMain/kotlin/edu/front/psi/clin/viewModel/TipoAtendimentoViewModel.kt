package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.TipoAtendimentoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.TipoAtendimento
import edu.front.psi.clin.navigation.TipoAtendimentoRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TipoAtendimentoViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val tipoApi = TipoAtendimentoAPI(httpClient)

    private val _tiposAtendimento = MutableStateFlow<List<TipoAtendimento>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _tiposAtendimento.value = tipoApi.carregar()
                println("Tipos de atendimento carregados no init: ${_tiposAtendimento.value}")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar tipos de atendimento do servidor: ${e.message}")
            }
        }
    }

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

    val backStack = NavBackStack<TipoAtendimentoRotas>(
        TipoAtendimentoRotas.Listagem
    )

    val id = mutableStateOf("")
    
    val nome = CampoValidado()
    val descricao = CampoValidado()
    val duracao = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val ativo = CampoValidado("true")
    val dataCadastro = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
        val v1 = nome.validar()
        val v2 = descricao.validar()
        val v3 = duracao.validar()
        val v4 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 )) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val tipo = TipoAtendimento(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    descricao = descricao.valor.value,
                    duracao = duracao.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    dataCadastro = dataCadastro.valor.value
                )

                if (id.value.isEmpty()) {
                    tipoApi.salvar(tipo)
                } else {
                    val idNumber = id.value.toLong()
                    tipoApi.atualizar(idNumber, tipo)
                }

                _tiposAtendimento.value = tipoApi.carregar()
                println("Tipos após salvar: ${_tiposAtendimento.value}")

                mensagem.value = "Tipo de atendimento salvo com sucesso!"
                mandarMensagem("Tipo de atendimento salvo com sucesso!")
                limparCampos()
                
                backStack.clear()
                backStack.add(TipoAtendimentoRotas.Listagem)

            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(tipo: TipoAtendimento) {
        viewModelScope.launch {
            try {
                tipoApi.apagar(tipo)
                _tiposAtendimento.value = tipoApi.carregar()
                println("Tipos após apagar: ${_tiposAtendimento.value}")
                mandarMensagem("Tipo de atendimento apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novoTipo() {
        backStack.clear()
        backStack.add(TipoAtendimentoRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(tipo: TipoAtendimento) {
        backStack.clear()
        backStack.add(TipoAtendimentoRotas.Formulario)
        id.value = tipo.id.toString()
        nome.definir(tipo.nome)
        descricao.definir(tipo.descricao)
        duracao.definir(tipo.duracao)
        ativo.definir(tipo.ativo.toString())
        dataCadastro.definir(tipo.dataCadastro)
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.limpar()
        descricao.limpar()
        duracao.limpar()
        ativo.definir("true")
        dataCadastro.limpar()
        mensagem.value = "Campos limpos."
    }
}