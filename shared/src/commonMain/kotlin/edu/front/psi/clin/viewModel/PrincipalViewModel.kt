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
    
    // Estado do menu lateral (Gaveta)
    val drawerState = DrawerState(DrawerValue.Closed)

    // Novo estado para a NotificacaoFlutuante
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

    // Função substituída para controlar o balão flutuante
    fun mostrarNotificacao(msg: String) {
        mensagemNotificacao = msg
        scope.launch {
            kotlinx.coroutines.delay(3500) // Some sozinha após 3.5 segundos
            if (mensagemNotificacao == msg) {
                mensagemNotificacao = null
            }
        }
    }
}