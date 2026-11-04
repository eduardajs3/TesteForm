package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface PagamentoRotas : NavKey {

    @Serializable
    data object Listagem : PagamentoRotas

    @Serializable
    data object Formulario : PagamentoRotas
}