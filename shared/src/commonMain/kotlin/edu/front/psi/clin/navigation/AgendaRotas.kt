package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AgendaRotas : NavKey {

    @Serializable
    data object Listagem : AgendaRotas

    @Serializable
    data object Formulario : AgendaRotas
}