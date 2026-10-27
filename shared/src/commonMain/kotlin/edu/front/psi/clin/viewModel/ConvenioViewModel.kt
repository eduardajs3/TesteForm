package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Convenio // Ajuste para o caminho do seu model
import kotlinx.coroutines.flow.*

class ConvenioViewModel : ViewModel() {

    private var idCounter: Int = 0
    private val _convenios = MutableStateFlow<List<Convenio>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val conveniosFiltrados = combine(_convenios, _filtro) { convenios, filtro ->
        if (filtro.isEmpty()) {
            convenios
        } else {
            convenios.filter { it.nome.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    val id = mutableStateOf("")
    val nome = mutableStateOf("")
    val cnpj = mutableStateOf("")
    val telefone = mutableStateOf("")
    val percentualCobertura = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val convenio = Convenio(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.value,
                cnpj = cnpj.value,
                telefone = telefone.value,
                percentualCobertura = percentualCobertura.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _convenios.update { lista -> lista + convenio.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _convenios.update { lista ->
                    lista.map { c -> if (c.id == idNumber) convenio.copy(id = idNumber) else c }
                }
            }
            mensagem.value = "Convênio salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(convenio: Convenio) {
        _convenios.update { lista -> lista - convenio }
    }

    fun novoConvenio() {
        tela.value = "form"
        id.value = ""
        nome.value = ""
        cnpj.value = ""
        telefone.value = ""
        percentualCobertura.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun editar(convenio: Convenio) {
        tela.value = "form"
        id.value = convenio.id.toString()
        nome.value = convenio.nome
        cnpj.value = convenio.cnpj
        telefone.value = convenio.telefone
        percentualCobertura.value = convenio.percentualCobertura.toString()
        ativo.value = convenio.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.value = ""
        cnpj.value = ""
        telefone.value = ""
        percentualCobertura.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}