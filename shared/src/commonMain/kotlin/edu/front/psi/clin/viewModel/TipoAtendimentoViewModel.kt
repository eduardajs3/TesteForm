package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.TipoAtendimentoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.TipoAtendimento
import edu.front.psi.clin.model.enums.ModalidadeAtendimento
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

    var salvando by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                _tiposAtendimento.value = tipoApi.carregar()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar tipos de atendimento do servidor"))
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
            tipos.filter {
                it.nome.contains(filtro, ignoreCase = true) ||
                    it.modalidade.rotulo.contains(filtro, ignoreCase = true) ||
                    it.modalidade.name.contains(filtro, ignoreCase = true)
            }
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
    val modalidade = CampoValidado(ModalidadeAtendimento.PRESENCIAL.name)

    fun salvar() {
        if (salvando) return

        val v1 = nome.validar()
        val v2 = descricao.validar()
        val v3 = duracao.validar()
        val v4 = ativo.validar()
        val v5 = modalidade.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val tipo = TipoAtendimento(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    descricao = descricao.valor.value,
                    duracao = duracao.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    modalidade = ModalidadeAtendimento.deValorApi(modalidade.valor.value)
                )

                if (id.value.isEmpty()) {
                    tipoApi.salvar(tipo)
                } else {
                    val idNumber = id.value.toLong()
                    tipoApi.atualizar(idNumber, tipo)
                }

                _tiposAtendimento.value = tipoApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Tipo de atendimento salvo com sucesso!"
                } else {
                    "Tipo de atendimento atualizado com sucesso!"
                }

                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(TipoAtendimentoRotas.Listagem)
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar"))
            } finally {
                salvando = false
            }
        }
    }

    fun apagar(tipo: TipoAtendimento) {
        viewModelScope.launch {
            try {
                tipoApi.apagar(tipo)
                _tiposAtendimento.value = tipoApi.carregar()
                mandarMensagem("Tipo de atendimento apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar"))
            }
        }
    }

    fun novoTipo() {
        if (backStack.lastOrNull() != TipoAtendimentoRotas.Formulario) {
            backStack.add(TipoAtendimentoRotas.Formulario)
        }
        id.value = ""
        limparCampos()
    }

    fun editar(tipo: TipoAtendimento) {
        if (backStack.lastOrNull() != TipoAtendimentoRotas.Formulario) {
            backStack.add(TipoAtendimentoRotas.Formulario)
        }
        id.value = tipo.id?.toString() ?: ""
        nome.definir(tipo.nome)
        descricao.definir(tipo.descricao)
        duracao.definir(tipo.duracao.take(5))
        ativo.definir(tipo.ativo.toString())
        modalidade.definir(tipo.modalidade.name)
    }

    fun limparCampos() {
        nome.limpar()
        descricao.limpar()
        duracao.limpar()
        ativo.definir("true")
        modalidade.definir(ModalidadeAtendimento.PRESENCIAL.name)
    }
}