package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface HorarioDisponivelRotas : NavKey {

    @Serializable
    data object Listagem : HorarioDisponivelRotas

    @Serializable
    data object Formulario : HorarioDisponivelRotas
}