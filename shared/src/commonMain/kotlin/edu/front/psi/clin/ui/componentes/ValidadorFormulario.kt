package edu.front.psi.clin.ui.componentes

import androidx.compose.runtime.mutableStateOf

class CampoValidado(
    valorInicial: String = "",
    private val formatador: ((String) -> String)? = null,
    private val validadorExtra: ((String) -> Boolean)? = null,
    private val mensagemErroPersonalizada: String = "Campo inválido"
) {
    val valor = mutableStateOf(if (formatador != null) formatador(valorInicial) else valorInicial)
    val temErro = mutableStateOf(false)
    val mensagemErro = mutableStateOf("Campo obrigatório")

    fun validar(): Boolean {
        val texto = valor.value.trim()
        if (texto.isBlank()) {
            temErro.value = true
            mensagemErro.value = "Campo obrigatório"
            return false
        }

        if (validadorExtra != null && !validadorExtra(texto)) {
            temErro.value = true
            mensagemErro.value = mensagemErroPersonalizada
            return false
        }

        temErro.value = false
        return true
    }

    fun limpar() {
        valor.value = ""
        temErro.value = false
        mensagemErro.value = "Campo obrigatório"
    }

    fun definir(novoValor: String) {
        val valorFormatado = if (formatador != null) formatador(novoValor) else novoValor
        valor.value = valorFormatado

        if (temErro.value && valorFormatado.isNotBlank()) {
            temErro.value = false
        }
    }
}
