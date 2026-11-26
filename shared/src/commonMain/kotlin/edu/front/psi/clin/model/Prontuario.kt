package edu.front.psi.clin.model

import edu.front.psi.clin.model.enums.ProntuarioStatus
import kotlinx.serialization.Serializable

@Serializable
data class Prontuario(
    val id: Long? = null,
    val pacienteId: Long,
    val dataAbertura: String,
    val queixaPrincipal: String,
    val status: ProntuarioStatus,
    val ativo: Boolean
)