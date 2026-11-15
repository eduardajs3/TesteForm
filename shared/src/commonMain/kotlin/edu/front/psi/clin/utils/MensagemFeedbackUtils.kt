package edu.front.psi.clin.utils

object MensagemFeedbackUtils {
    fun ehErro(mensagem: String): Boolean {
        val texto = mensagem.trim().lowercase()
        if (texto.isEmpty()) return false

        val erroPadrao = listOf(
            "erro",
            "não foi possível",
            "ja existe",
            "já existe",
            "nao foi possivel",
            "já possui",
            "ja possui",
            "já está",
            "ja esta",
            "já foi",
            "ja foi",
            "já cadastrado",
            "ja cadastrado",
            "não encontrado",
            "nao encontrado",
            "inválido",
            "invalido",
            "não pode",
            "nao pode",
            "negado",
            "credenciais inválidas",
            "credenciais invalidas",
            "acesso negado",
            "preencha",
            "selecione",
            "por favor",
            "o horário de término",
            "o horario de termino",
            "a descrição da evolução",
            "a descricao da evolucao",
            "obrigatório",
            "obrigatorio",
            "não está",
            "nao esta",
            "não pode estar",
            "nao pode estar"
        )

        return erroPadrao.any { texto.contains(it) }
    }
}
