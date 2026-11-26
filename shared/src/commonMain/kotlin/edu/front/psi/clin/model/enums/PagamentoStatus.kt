package edu.front.psi.clin.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class PagamentoStatus(val rotulo: String) {
    PENDENTE("Pendente"),
    PAGO("Pago"),
    CANCELADO("Cancelado");

    companion object {
        fun deValorApi(valor: String): PagamentoStatus =
            entries.firstOrNull { it.name.equals(valor, ignoreCase = true) } ?: PENDENTE
    }
}