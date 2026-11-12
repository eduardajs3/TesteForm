package edu.front.psi.clin.model

import kotlinx.serialization.Serializable

@Serializable
data class Recepcionista(
    val id: Long? = null,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String,
    val senha: String? = null,
    val ativo: Boolean = true,
    val dataCadastro: String? = null,
    val setor: String,
    val role: String? = null // Permite que venha nulo sem quebrar a desserialização
)