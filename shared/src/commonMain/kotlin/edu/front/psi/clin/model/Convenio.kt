package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class Convenio(
    val id: Int,
    val nome: String,
    val cnpj: String,
    val telefone: String,
    val percentualCobertura: Double,
    val ativo: Boolean
)
