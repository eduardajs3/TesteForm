package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.TipoAtendimento
import edu.front.psi.clin.ui.components.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*

class TipoAtendimentoViewModel : ViewModel() {

    private var idCounter: Int = 0
    private val _tiposAtendimento = MutableStateFlow<List<TipoAtendimento>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

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

    val tela = mutableStateOf("list") // "list" ou "form"

    val id = mutableStateOf("")
    
    // Campos do formulário geridos pelo validador (duração com máscara de hora)
    val nome = CampoValidado()
    val descricao = CampoValidado()
    val duracao = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val ativo = CampoValidado("true")
    val dataCadastro = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios
        val v1 = nome.validar()
        val v2 = descricao.validar()
        val v3 = duracao.validar()
        val v4 = ativo.validar()
        val v5 = dataCadastro.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            return
        }

        try {
            val tipo = TipoAtendimento(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.valor.value,
                descricao = descricao.valor.value,
                duracao = duracao.valor.value,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true,
                dataCadastro = dataCadastro.valor.value
            )

            if (id.value.isEmpty()) {
                idCounter++
                _tiposAtendimento.update { lista -> lista + tipo.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _tiposAtendimento.update { lista ->
                    lista.map { t -> if (t.id == idNumber) tipo.copy(id = idNumber) else t }
                }
            }
            mensagem.value = "Tipo de atendimento salvo com sucesso!"
            limparCampos()
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(tipo: TipoAtendimento) {
        _tiposAtendimento.update { lista -> lista - tipo }
    }

    fun novoTipo() {
        tela.value = "form"
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(tipo: TipoAtendimento) {
        tela.value = "form"
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