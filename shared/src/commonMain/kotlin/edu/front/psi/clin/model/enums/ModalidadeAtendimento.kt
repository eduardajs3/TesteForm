package edu.front.psi.clin.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ModalidadeAtendimento(val rotulo: String) {
    PRESENCIAL("Presencial"),
    ONLINE("Online"),
    HIBRIDO("Híbrido");

    companion object {
        fun deValorApi(valor: String): ModalidadeAtendimento =
            entries.firstOrNull { it.name.equals(valor, ignoreCase = true) } ?: PRESENCIAL
    }
}