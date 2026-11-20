package edu.front.psi.clin.api

object SessionManager {
    var token: String = ""
        private set

    var role: String? = null
        private set

    var email: String? = null
        private set

    fun iniciarSessao(accessToken: String, role: String, email: String) {
        token = accessToken
        this.role = edu.front.psi.clin.security.PermissoesAcesso.normalizarRole(role)
        this.email = email
    }

    fun encerrarSessao() {
        token = ""
        role = null
        email = null
    }
}
