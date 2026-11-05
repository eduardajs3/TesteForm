package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.Convenio
import edu.front.psi.clin.ui.components.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import edu.front.psi.clin.navigation.ConvenioRotas

class ConvenioViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private var idCounter: Int = 0
    private val _convenios = MutableStateFlow<List<Convenio>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()
    val mensagem = mutableStateOf("")

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

    val backStack =
        NavBackStack<ConvenioRotas>(
            ConvenioRotas.Listagem
        )

    val id = mutableStateOf("")
    
    val nome = CampoValidado()
    val cnpj = CampoValidado(formatador = { MaskUtils.aplicarMascaraCnpj(it) })
    val telefone = CampoValidado(formatador = { MaskUtils.aplicarMascaraTelefone(it) })
    val percentualCobertura = CampoValidado(
        formatador = { MaskUtils.aplicarMascaraPercentual(it) }
    )
    val ativo = CampoValidado("true")

    fun salvar() {
        val v1 = nome.validar()
        val v2 = cnpj.validar()
        val v3 = telefone.validar()
        val v4 = percentualCobertura.validar()
        val v5 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
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
            mandarMensagem("Convênio salvo com sucesso!")
            limparCampos()
         
            backStack.clear()
            backStack.add(
                ConvenioRotas.Listagem
            )
            
        } catch (e: Exception) {
            mandarMensagem("Erro ao salvar: ${e.message}")
        }
    }

    fun apagar(convenio: Convenio) {
        _convenios.update { lista -> lista - convenio }
        mandarMensagem("Convênio apagado com sucesso!")
    }

    fun novoConvenio() {
        id.value = ""
        limparCampos()
        backStack.clear()
        backStack.add(
            ConvenioRotas.Formulario
        )
    }

    fun editar(convenio: Convenio) {
        id.value = convenio.id.toString()
        nome.definir(convenio.nome)
        cnpj.definir(convenio.cnpj)
        telefone.definir(convenio.telefone)
        percentualCobertura.definir(convenio.percentualCobertura.toString())
        ativo.definir(convenio.ativo.toString())
        
        backStack.clear()
        backStack.add(
            ConvenioRotas.Formulario
        )
    }

    fun limparCampos() {
        nome.limpar()
        cnpj.limpar()
        telefone.limpar()
        percentualCobertura.limpar()
        ativo.definir("true")
    }
}