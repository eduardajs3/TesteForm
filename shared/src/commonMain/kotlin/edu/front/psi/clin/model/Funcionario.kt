package edu.front.psi.clin

data class Funcionario(
    val id: Int,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String,
    val senha: String,
    val ativo: Boolean,
    val dataCadastro: String
)
