package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class BloqueioAgenda(
    val id: Long? = null,
    val psicologoId: Long? = null,
    val dataHoraInicio: String? = null,
    val dataHoraFim: String? = null,
    val motivo: String = "",
    val ativo: Boolean = true
)
