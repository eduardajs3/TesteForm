package edu.front.psi.clin.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val senha: String
)

@Serializable
data class LoginResponse(
    @SerialName("token")
    val accessToken: String,
    val role: String
)

class AuthAPI(private val httpClient: HttpClient) {
    suspend fun login(email: String, senha: String): LoginResponse {
        val response = httpClient.post("${ApiConfig.BASE_URL}/auth/signin") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email = email, senha = senha))
        }

        if (response.status.value !in 200..299) {
            throw IllegalStateException("E-mail ou senha inválidos (${response.status.value}).")
        }

        val loginResponse = response.body<LoginResponse>()
        if (loginResponse.accessToken.isBlank()) {
            throw IllegalStateException("O servidor não retornou um token de acesso.")
        }
        if (!edu.front.psi.clin.security.PermissoesAcesso.roleConhecida(loginResponse.role)) {
            throw IllegalStateException("O servidor retornou um perfil de acesso inválido.")
        }

        return loginResponse
    }
}