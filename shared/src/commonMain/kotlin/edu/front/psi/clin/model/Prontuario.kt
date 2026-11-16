package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class Prontuario(
    val id: Long? = null,
    val pacienteId: Long? = null,
    val dataAbertura: String? = null,
    val queixaPrincipal: String = "",
    val status: String? = null,
    val ativo: Boolean = true
)
