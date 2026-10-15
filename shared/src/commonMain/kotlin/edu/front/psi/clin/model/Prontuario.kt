package edu.front.psi.clin

data class Prontuario(
    val id: Int,
    val paciente: String,
    val dataAbertura: String,
    val queixaPrincipal: String,
    val status: String,
    val ativo: Boolean
)
