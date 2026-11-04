package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface TipoAtendimentoRotas : NavKey {

    @Serializable
    data object Listagem : TipoAtendimentoRotas

    @Serializable
    data object Formulario : TipoAtendimentoRotas
}