package edu.front.psi.clin.api

import kotlinx.serialization.Serializable

@Serializable
internal data class AlterarSenhaRequest(
    val novaSenha: String
)
