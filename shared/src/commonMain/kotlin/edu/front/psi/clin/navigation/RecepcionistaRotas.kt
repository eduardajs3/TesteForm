package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface RecepcionistaRotas : NavKey {

    @Serializable
    data object Listagem : RecepcionistaRotas

    @Serializable
    data object Formulario : RecepcionistaRotas
}