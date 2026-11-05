package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.SalaAtendimento
import edu.front.psi.clin.navigation.SalaAtendimentoRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import kotlinx.coroutines.flow.*

class SalaAtendimentoViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private var idCounter: Int = 0

    private val _salas = MutableStateFlow<List<SalaAtendimento>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

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

    // Campos do formulário geridos pelo validador
    val id = mutableStateOf("")
    val nome = CampoValidado()
    val descricao = CampoValidado()
    val capacidade = CampoValidado()
    val ativo = CampoValidado("true")
    val dataCadastro = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios
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

        try {
            val sala = SalaAtendimento(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.valor.value,
                descricao = descricao.valor.value,
                capacidade = capacidade.valor.value.toIntOrNull() ?: 0,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.valor.value
            )

            if (id.value.isEmpty()) {
                idCounter++
                _salas.update { lista -> lista + sala.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _salas.update { lista ->
                    lista.map { s -> if (s.id == idNumber) sala.copy(id = idNumber) else s }
                }
            }
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

    fun apagar(sala: SalaAtendimento) {
        _salas.update { lista -> lista - sala }
        mandarMensagem("Sala apagada com sucesso!")
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