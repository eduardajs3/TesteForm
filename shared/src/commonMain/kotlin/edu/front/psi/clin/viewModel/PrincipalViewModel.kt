package edu.front.psi.clin.ui.viewmodel

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.api.AuthAPI
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PrincipalViewModel(private val scope: CoroutineScope) : ViewModel() {

    val backstack = mutableStateListOf<Route>(Route.Login)
    private val authAPI = AuthAPI(httpClientManager())

    var autenticado by mutableStateOf(false)
        private set

    var autenticando by mutableStateOf(false)
        private set

    var erroLogin by mutableStateOf<String?>(null)
        private set

    val role: String?
        get() = SessionManager.role

    val usuarioConectado: String?
        get() = SessionManager.email

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

    fun voltar() {
        if (backstack.size > 1) {
            backstack.removeAt(backstack.lastIndex)
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

    fun login(email: String, senha: String) {
        if (autenticando) return

        viewModelScope.launch {
            autenticando = true
            erroLogin = null
            try {
                val resposta = authAPI.login(email.trim(), senha)
                SessionManager.iniciarSessao(resposta.accessToken, resposta.role, email.trim())
                autenticado = true
                backstack.clear()
                backstack.add(Route.Dashboard)
            } catch (e: Exception) {
                SessionManager.encerrarSessao()
                erroLogin = e.message ?: "Não foi possível entrar. Tente novamente."
            } finally {
                autenticando = false
            }
        }
    }

    fun logout() {
        SessionManager.encerrarSessao()
        autenticado = false
        erroLogin = null
        backstack.clear()
        backstack.add(Route.Login)
    }
}