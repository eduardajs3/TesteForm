package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class Convenio(
    val id: Long? = null,
    val nome: String,
    val cnpj: String,
    val telefone: String,
    val percentualCobertura: Double? = null,
    val ativo: Boolean
)
