package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.SalaAtendimentoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
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

    var salvando by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                _salas.value = salaApi.carregar()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar salas do servidor"))
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
    var recursosTexto by mutableStateOf("")
    var recursosDisponiveis by mutableStateOf<List<String>>(emptyList())
        private set

    fun definirRecursosTexto(texto: String) {
        recursosTexto = texto
        recursosDisponiveis = texto
            .split(',')
            .map(String::trim)
            .filter(String::isNotEmpty)
    }

    fun salvar() {
        if (salvando) return

        val v1 = nome.validar()
        val v2 = descricao.validar()
        val v3 = capacidade.validar()
        val v4 = ativo.validar()

        if (!(v1 && v2 && v3 && v4)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val sala = SalaAtendimento(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    descricao = descricao.valor.value,
                    capacidade = capacidade.valor.value.toIntOrNull() ?: 0,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                    recursosDisponiveis = recursosDisponiveis
                )

                if (id.value.isEmpty()) {
                    salaApi.salvar(sala)
                } else {
                    val idNumber = id.value.toLong()
                    salaApi.atualizar(idNumber, sala)
                }

                _salas.value = salaApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Sala salva com sucesso!"
                } else {
                    "Sala atualizada com sucesso!"
                }

                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(SalaAtendimentoRotas.Listagem)
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar"))
            } finally {
                salvando = false
            }
        }
    }

    fun apagar(sala: SalaAtendimento) {
        viewModelScope.launch {
            try {
                salaApi.apagar(sala)
                _salas.value = salaApi.carregar()
                mandarMensagem("Sala apagada com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar"))
            }
        }
    }

    fun novaSala() {
        if (backStack.lastOrNull() != SalaAtendimentoRotas.Formulario) {
            backStack.add(SalaAtendimentoRotas.Formulario)
        }
        id.value = ""
        limparCampos()
    }

    fun editar(sala: SalaAtendimento) {
        if (backStack.lastOrNull() != SalaAtendimentoRotas.Formulario) {
            backStack.add(SalaAtendimentoRotas.Formulario)
        }
        id.value = sala.id?.toString() ?: ""
        nome.definir(sala.nome)
        descricao.definir(sala.descricao)
        capacidade.definir(sala.capacidade.toString())
        ativo.definir(sala.ativo.toString())
        recursosDisponiveis = sala.recursosDisponiveis
        recursosTexto = sala.recursosDisponiveis.joinToString(", ")
    }

    fun limparCampos() {
        nome.limpar()
        descricao.limpar()
        capacidade.limpar()
        ativo.definir("true")
        recursosTexto = ""
        recursosDisponiveis = emptyList()
    }
}