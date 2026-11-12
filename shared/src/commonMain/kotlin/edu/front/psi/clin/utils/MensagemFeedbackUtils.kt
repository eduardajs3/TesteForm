package edu.front.psi.clin.utils

object MensagemFeedbackUtils {
    fun ehErro(mensagem: String): Boolean {
        val texto = mensagem.trim().lowercase()
        return texto.startsWith("erro") ||
            texto.startsWith("preencha") ||
            texto.startsWith("selecione") ||
            texto.startsWith("por favor") ||
            texto.startsWith("o horário de término") ||
            texto.startsWith("a descrição da evolução")
    }
}
