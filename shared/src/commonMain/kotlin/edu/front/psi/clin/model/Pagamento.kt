package edu.front.psi.clin.model

import kotlinx.serialization.Serializable

@Serializable
data class Pagamento(
    val id: Long? = null,
    val agendaId: Long,
    val pacienteId: Long,
    val convenioId: Long,
    val valorTotal: Double,
    val valorConvenio: Double,
    val valorPaciente: Double,
    val formaPagamento: String,
    val dataPagamento: String,
    val status: String
)