package edu.front.psi.clin

data class Pagamento(
    val id: Int,
    val paciente: String,
    val convenio: String,
    val valorTotal: Double,
    val valorConvenio: Double,
    val valorPaciente: Double,
    val formaPagamento: String,
    val dataPagamento: String,
    val status: String
)
