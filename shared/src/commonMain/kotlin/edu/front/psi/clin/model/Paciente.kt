package edu.front.psi.clin

data class Paciente(
    val id: Int,
    val nome: String,
    val cpf: String,
    val dataNascimento: String,
    val email: String,
    val telefone: String,
    val endereco: String,
    val observacoes: String,
    val ativo: Boolean
)