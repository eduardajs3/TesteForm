package edu.front.psi.clin.ui.components

import androidx.compose.runtime.mutableStateOf

/**
 * Classe utilitária para gerir o valor, estado de preenchimento, validação 
 * e formatação de campos de formulário no Jetpack Compose.
 */
class CampoValidado(
    valorInicial: String = "",
    private val formatador: ((String) -> String)? = null,
    private val validadorExtra: ((String) -> Boolean)? = null, // Regra extra (ex: validar e-mail)
    private val mensagemErroPersonalizada: String = "Campo inválido" // Mensagem customizável para o erro
) {
    val valor = mutableStateOf(if (formatador != null) formatador(valorInicial) else valorInicial)
    val temErro = mutableStateOf(false)
    val mensagemErro = mutableStateOf("Campo obrigatório")

    /**
     * Valida se o campo está preenchido e se cumpre a regra extra (se houver).
     */
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

    /**
     * Limpa o valor do campo e remove qualquer estado de erro visual.
     */
    fun limpar() {
        valor.value = ""
        temErro.value = false
        mensagemErro.value = "Campo obrigatório"
    }
    
    /**
     * Define um novo valor para o campo aplicando o formatador/máscara automaticamente.
     */
    fun definir(novoValor: String) {
        val valorFormatado = if (formatador != null) formatador(novoValor) else novoValor
        valor.value = valorFormatado
        
        if (temErro.value && valorFormatado.isNotBlank()) {
            temErro.value = false
        }
    }
}