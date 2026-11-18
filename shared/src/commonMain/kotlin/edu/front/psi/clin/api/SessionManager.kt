package edu.front.psi.clin.api

object SessionManager {
    var token: String = ""
        private set

    var role: String? = null
        private set

    fun iniciarSessao(accessToken: String, role: String) {
        token = accessToken
        this.role = edu.front.psi.clin.security.PermissoesAcesso.normalizarRole(role)
    }

    fun encerrarSessao() {
        token = ""
        role = null
    }
}