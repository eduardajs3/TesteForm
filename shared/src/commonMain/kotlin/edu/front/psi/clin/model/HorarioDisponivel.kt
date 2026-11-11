package edu.front.psi.clin.model

import kotlinx.serialization.Serializable

@Serializable
data class HorarioDisponivel(
    val id: Long? = null,
    val psicologoId: Long,
    val psicologo: String? = null,
    val diaSemana: String,
    val horaInicio: String,
    val horaFim: String,
    val descricao: String? = null,
    val ativo: Boolean = true
)