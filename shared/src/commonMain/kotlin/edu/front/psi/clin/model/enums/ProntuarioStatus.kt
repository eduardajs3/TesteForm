package edu.front.psi.clin.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ProntuarioStatus(val rotulo: String) {
    ATIVO("Ativo"),
    ARQUIVADO("Arquivado"),
    ENCERRADO("Encerrado");

    companion object {
        fun deValorApi(valor: String): ProntuarioStatus =
            entries.firstOrNull {
                it.name.equals(valor.trim(), ignoreCase = true) ||
                    it.rotulo.equals(valor.trim(), ignoreCase = true)
            } ?: when (valor.trim().lowercase()) {
                "false", "0", "inativo", "não", "nao" -> ENCERRADO
                else -> ATIVO
            }
    }
}