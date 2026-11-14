package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class Funcionario(
    val id: Long? = null,
    val nome: String? = null,
    val cpf: String? = null,
    val email: String? = null,
    val telefone: String? = null,
    val senha: String? = null,
    val ativo: Boolean? = true,
    val dataCadastro: String? = null,
    val role: String? = null
)
