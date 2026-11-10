package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class Agenda(
    val id: Int,
    val paciente: String,
    val psicologo: String,
    val sala: String,
    val tipoAtendimento: String,
    val convenio: String,
    val dataHoraInicio: String,
    val dataHoraFim: String,
    val status: String,
    val observacoes: String,
    val ativo: Boolean
)
