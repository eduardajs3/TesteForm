package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class EvolucaoClinica(
    val id: Long? = null,
    val prontuarioId: Long,
    val psicologoId: Long,
    val agendaId: Long,
    val dataRegistro: String,
    val descricao: String? = null,
    val ativo: Boolean
)
