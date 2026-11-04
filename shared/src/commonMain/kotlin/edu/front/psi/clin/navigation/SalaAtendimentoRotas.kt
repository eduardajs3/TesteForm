package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface SalaAtendimentoRotas : NavKey {

    @Serializable
    data object Listagem : SalaAtendimentoRotas

    @Serializable
    data object Formulario : SalaAtendimentoRotas
}