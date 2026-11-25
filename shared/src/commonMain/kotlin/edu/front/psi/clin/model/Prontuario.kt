package edu.front.psi.clin.model

import kotlinx.serialization.Serializable

@Serializable
data class Prontuario(
    val id: Long? = null,
    val pacienteId: Long,
    val dataAbertura: String,
    val queixaPrincipal: String,
    val status: String,
    val ativo: Boolean 
)