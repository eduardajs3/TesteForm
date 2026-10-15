package edu.front.psi.clin

data class Psicologo(
    val id: Int,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String,
    val senha: String,
    val ativo: Boolean,
    val crp: String,
    val especialidade: String
)
