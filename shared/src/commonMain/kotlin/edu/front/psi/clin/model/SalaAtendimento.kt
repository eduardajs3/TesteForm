package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class SalaAtendimento(
    val id: Long? = null,
    val nome: String,
    val descricao: String,
    val capacidade: Int,
    val ativo: Boolean,
    val dataCadastro: String = ""
)
