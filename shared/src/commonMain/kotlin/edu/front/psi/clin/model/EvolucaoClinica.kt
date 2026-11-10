package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class EvolucaoClinica(
    val id: Int,
    val prontuario: String,
    val psicologo: String,
    val agenda: String,
    val dataRegistro: String,
    val descricao: String,
    val ativo: Boolean
)
