
package edu.front.psi.clin

data class SalaAtendimento(
    val id: Int,
    val nome: String,
    val descricao: String,
    val capacidade: Int,
    val ativo: Boolean,
    val dataCadastro: String
)
