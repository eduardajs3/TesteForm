package edu.front.psi.clin.model

import kotlinx.serialization.Serializable

@Serializable
data class Agenda(
    val id: Long? = null,
    val pacienteId: Long,
    val psicologoId: Long,
    val salaId: Long,
    val tipoAtendimentoId: Long,
    val convenioId: Long,
    val dataHoraInicio: String,
    val dataHoraFim: String,
    val status: String,
    val observacoes: String? = null,
    val ativo: Boolean
)