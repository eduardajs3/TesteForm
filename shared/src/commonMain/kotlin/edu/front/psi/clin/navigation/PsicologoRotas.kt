package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface PsicologoRotas : NavKey {

    @Serializable
    data object Listagem : PsicologoRotas

    @Serializable
    data object Formulario : PsicologoRotas
}