package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class Psicologo(
    val id: Long? = null,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String,
    val senha: String? = null,
    val ativo: Boolean,
    val crp: String,
    val especialidade: String
)
