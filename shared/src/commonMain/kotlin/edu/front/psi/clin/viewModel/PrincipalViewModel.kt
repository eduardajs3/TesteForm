package edu.front.psi.clin.ui.viewmodel

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf

class PrincipalViewModel(private val scope: CoroutineScope) : ViewModel() {

    val backstack = mutableStateListOf<Route>(Route.Login)


    val drawerState = DrawerState(DrawerValue.Closed)


    var mensagemNotificacao by mutableStateOf<String?>(null)
        private set

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


    fun mostrarNotificacao(msg: String) {
        mensagemNotificacao = msg
        scope.launch {
            kotlinx.coroutines.delay(3500)
            if (mensagemNotificacao == msg) {
                mensagemNotificacao = null
            }
        }
    }
}