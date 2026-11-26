package edu.front.psi.clin.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class AgendaStatus(val rotulo: String) {
    AGENDADO("Agendado"),
    CONFIRMADO("Confirmado"),
    REALIZADO("Realizado"),
    CANCELADO("Cancelado"),
    FALTOU("Faltou");

    companion object {
        fun deValorApi(valor: String): AgendaStatus =
            entries.firstOrNull {
                it.name.equals(valor.trim(), ignoreCase = true) ||
                    it.rotulo.equals(valor.trim(), ignoreCase = true)
            } ?: when (valor.trim().uppercase()) {
                "CANCELED" -> CANCELADO
                else -> AGENDADO
            }

        val opcoesNovoAgendamento: List<AgendaStatus> = listOf(AGENDADO, CONFIRMADO)
    }
}