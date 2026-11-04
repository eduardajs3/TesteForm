package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface BloqueioAgendaRotas : NavKey {

    @Serializable
    data object Listagem : BloqueioAgendaRotas

    @Serializable
    data object Formulario : BloqueioAgendaRotas
}