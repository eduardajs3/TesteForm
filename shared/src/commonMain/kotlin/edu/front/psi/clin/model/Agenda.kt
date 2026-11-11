// shared/src/commonMain/kotlin/edu/front/psi/clin/model/Agenda.kt
package edu.front.psi.clin.model

import kotlinx.serialization.Serializable
@Serializable
data class Agenda(
    val id: Long? = null,

    val pacienteId: Long,
    val paciente: String? = null,

    val psicologoId: Long,
    val psicologo: String? = null,

    val salaId: Long,
    val sala: String? = null,

    val tipoAtendimentoId: Long,
    val tipoAtendimento: String? = null,

    val convenioId: Long,
    val convenio: String? = null,

    val dataHoraInicio: String,
    val dataHoraFim: String,

    val status: String,
    val observacoes: String? = null,
    val ativo: Boolean = true
)