package edu.front.psi.clin.model

data class Prontuario(
    val id: Int,
    val paciente: String,
    val dataAbertura: String,
    val queixaPrincipal: String,
    val status: String,
    val ativo: Boolean
)
