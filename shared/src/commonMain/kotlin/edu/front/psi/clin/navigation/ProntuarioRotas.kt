package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ProntuarioRotas : NavKey {

    @Serializable
    data object Listagem : ProntuarioRotas

    @Serializable
    data object Formulario : ProntuarioRotas
}