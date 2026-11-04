package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface EvolucaoClinicaRotas : NavKey {

    @Serializable
    data object Listagem : EvolucaoClinicaRotas

    @Serializable
    data object Formulario : EvolucaoClinicaRotas
}