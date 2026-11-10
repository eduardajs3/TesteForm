package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class Prontuario(
    val id: Int,
    val paciente: String,
    val dataAbertura: String,
    val queixaPrincipal: String,
    val status: String,
    val ativo: Boolean
)
