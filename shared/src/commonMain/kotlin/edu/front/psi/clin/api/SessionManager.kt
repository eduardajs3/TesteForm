package edu.front.psi.clin.api

import edu.front.psi.clin.security.PermissoesAcesso

object SessionManager {
    var token: String = ""
        private set

    var role: String? = null
        private set

    var email: String? = null
        private set

    private val aoAlterarSessao = mutableListOf<() -> Unit>()

    fun aoAlterar(callback: () -> Unit) {
        if (callback !in aoAlterarSessao) {
            aoAlterarSessao.add(callback)
        }
    }

    private fun notificarAlteracaoSessao() {
        aoAlterarSessao.toList().forEach { it.invoke() }
    }

    fun iniciarSessao(accessToken: String, role: String, email: String) {
        var normalized = accessToken.trim()
        val bearerPrefix = Regex("^Bearer\\s+", RegexOption.IGNORE_CASE)
        while (bearerPrefix.containsMatchIn(normalized)) {
            normalized = bearerPrefix.replace(normalized, "").trim()
        }
        token = normalized
        this.role = PermissoesAcesso.normalizarRole(role)
        this.email = email
        notificarAlteracaoSessao()
    }

    fun encerrarSessao() {
        token = ""
        role = null
        email = null
        notificarAlteracaoSessao()
    }
}