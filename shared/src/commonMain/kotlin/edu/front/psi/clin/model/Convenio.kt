
package edu.front.psi.clin

data class Convenio(
    val id: Int,
    val nome: String,
    val cnpj: String,
    val telefone: String,
    val percentualCobertura: Double,
    val ativo: Boolean
)
