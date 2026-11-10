package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class HorarioDisponivel(
    val id: Int,
    val psicologo: String,
    val diaSemana: String,
    val horaInicio: String,
    val horaFim: String,
    val descricao: String,
    val ativo: Boolean
)
