package edu.front.psi.clin.api

object SessionManager {
    private var token: String? = null

    fun configure(token: String?) {
        this.token = token?.trim()?.takeIf(String::isNotEmpty)
    }

    fun requireToken(): String =
        token ?: error("Token ausente. Defina a variavel de ambiente PSICLIN_API_TOKEN.")
}
