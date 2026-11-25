package edu.front.psi.clin.api

import edu.front.psi.clin.model.Paciente
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PacienteAPI(private val httpClient: HttpClient) {
    private val endPoint = "/pacientes"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Paciente> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(paciente: Paciente) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(paciente)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, paciente: Paciente) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(paciente)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(paciente: Paciente) {
        val id = requireNotNull(paciente.id) { "Paciente sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}