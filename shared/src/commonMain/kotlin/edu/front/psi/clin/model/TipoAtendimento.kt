package edu.front.psi.clin

data class TipoAtendimento(
    val id: Int,
    val nome: String,
    val descricao: String,
    val duracao: String,
    val ativo: Boolean,
    val dataCadastro: String
)
