package edu.front.psi.clin.api

import edu.front.psi.clin.model.Prontuario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProntuarioAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/prontuarios"

    suspend fun carregar(): List<Prontuario> =
        httpClient.get(endPoint) {
            bearerAuth(SessionManager.requireToken())
        }.body()

    suspend fun salvar(prontuario: Prontuario) {
        httpClient.post(endPoint) {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(prontuario)
        }
    }

    suspend fun atualizar(id: Long, prontuario: Prontuario) {
        httpClient.put("$endPoint/$id") {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(prontuario)
        }
    }

    suspend fun apagar(prontuario: Prontuario) {
        httpClient.delete("$endPoint/${prontuario.id}") {
            bearerAuth(SessionManager.requireToken())
        }
    }
}
