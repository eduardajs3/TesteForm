package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
data class TipoAtendimento(
    val id: Long? = null,
    val nome: String,
    val descricao: String,
    val duracao: String,
    val ativo: Boolean,
    val dataCadastro: String = ""
)
