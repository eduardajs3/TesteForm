package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Convenio
import edu.front.psi.clin.ui.components.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
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
    
    // Campos geridos pelo validador com máscaras automáticas aplicadas
    val nome = CampoValidado()
    val cnpj = CampoValidado(formatador = { MaskUtils.aplicarMascaraCnpj(it) })
    val telefone = CampoValidado(formatador = { MaskUtils.aplicarMascaraTelefone(it) })
    val percentualCobertura = CampoValidado(
        formatador = { MaskUtils.aplicarMascaraPercentual(it) }
    )
    val ativo = CampoValidado("true")
    
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios
        val v1 = nome.validar()
        val v2 = cnpj.validar()
        val v3 = telefone.validar()
        val v4 = percentualCobertura.validar()
        val v5 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            return
        }

        try {
            val convenio = Convenio(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.valor.value,
                cnpj = cnpj.valor.value,
                telefone = telefone.valor.value,
                percentualCobertura = percentualCobertura.valor.value,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
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
            limparCampos()
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
        limparCampos()
        mensagem.value = ""
    }

    fun editar(convenio: Convenio) {
        tela.value = "form"
        id.value = convenio.id.toString()
        nome.definir(convenio.nome)
        cnpj.definir(convenio.cnpj)
        telefone.definir(convenio.telefone)
        percentualCobertura.definir(convenio.percentualCobertura.toString())
        ativo.definir(convenio.ativo.toString())
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.limpar()
        cnpj.limpar()
        telefone.limpar()
        percentualCobertura.limpar()
        ativo.definir("true")
        mensagem.value = "Campos limpos."
    }
}