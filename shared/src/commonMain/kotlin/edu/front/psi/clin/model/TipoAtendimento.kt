package edu.front.psi.clin.model

data class TipoAtendimento(
    val id: Int,
    val nome: String,
    val descricao: String,
    val duracao: String,
    val ativo: Boolean,
    val dataCadastro: String
)
