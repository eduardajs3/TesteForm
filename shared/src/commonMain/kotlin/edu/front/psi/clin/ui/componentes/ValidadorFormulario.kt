package edu.front.psi.clin.ui.components

import androidx.compose.runtime.mutableStateOf

/**
 * Classe utilitária para gerir o valor, estado de preenchimento e validação 
 * de campos de formulário no Jetpack Compose.
 */
class CampoValidado(valorInicial: String = "") {
    val valor = mutableStateOf(valorInicial)
    val temErro = mutableStateOf(false)

    /**
     * Valida se o campo está em branco. 
     * Retorna true se estiver válido (preenchido) e false se tiver erro.
     */
    fun validar(): Boolean {
        temErro.value = valor.value.isBlank()
        return !temErro.value
    }

    /**
     * Limpa o valor do campo e remove qualquer estado de erro visual.
     */
    fun limpar() {
        valor.value = ""
        temErro.value = false
    }
    
    /**
     * Define um novo valor para o campo (ideal para a digitação do utilizador ou carregamento de edição).
     * Se houver um erro ativo e o utilizador começar a preencher, o erro é removido automaticamente.
     */
    fun definir(novoValor: String) {
        valor.value = novoValor
        if (temErro.value && novoValor.isNotBlank()) {
            temErro.value = false
        }
    }
}