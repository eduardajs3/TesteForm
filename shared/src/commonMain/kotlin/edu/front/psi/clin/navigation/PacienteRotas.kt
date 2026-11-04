package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface PacienteRotas : NavKey {

    @Serializable
    data object Listagem : PacienteRotas

    @Serializable
    data object Formulario : PacienteRotas
}