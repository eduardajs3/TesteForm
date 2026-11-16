package edu.front.psi.clin.api

object SessionManager {
    var token: String = ""
        private set

    fun iniciarSessao(accessToken: String) {
        token = accessToken
    }

    fun encerrarSessao() {
        token = ""
    }
}