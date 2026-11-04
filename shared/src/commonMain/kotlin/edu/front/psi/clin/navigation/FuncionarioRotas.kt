package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface FuncionarioRotas : NavKey {

    @Serializable
    data object Listagem : FuncionarioRotas

    @Serializable
    data object Formulario : FuncionarioRotas
}