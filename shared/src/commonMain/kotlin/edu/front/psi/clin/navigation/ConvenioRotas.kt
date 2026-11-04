package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ConvenioRotas : NavKey {

    @Serializable
    data object Listagem : ConvenioRotas

    @Serializable
    data object Formulario : ConvenioRotas
}