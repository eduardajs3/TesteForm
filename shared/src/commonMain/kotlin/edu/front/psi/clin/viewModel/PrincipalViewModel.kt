package edu.front.psi.clin.ui.viewmodel

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PrincipalViewModel(private val scope: CoroutineScope) : ViewModel() {

    // Pilha de navegação global baseada no Navigation 3
    val backstack = NavBackStack<Route>(Route.Dashboard)
    
    // Estado do menu lateral (Gaveta)
    val drawerState = DrawerState(DrawerValue.Closed)

    // Estado global para mensagens flutuantes (Snackbars)
    val snackbarHostState = SnackbarHostState()

    fun fecharGaveta() {
        scope.launch {
            drawerState.close()
        }
    }

    fun abrirGaveta() {
        scope.launch {
            drawerState.open()
        }
    }

    fun mostrarMensagem(
        mensagem: String,
        duracao: SnackbarDuration = SnackbarDuration.Short
    ) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = mensagem,
                duration = duracao
            )
        }
    }
}