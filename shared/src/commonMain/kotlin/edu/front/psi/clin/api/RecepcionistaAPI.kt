package edu.front.psi.clin.api

import edu.front.psi.clin.model.Recepcionista
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RecepcionistaAPI(private val httpClient: HttpClient) {
    private val endPoint = "/recepcionistas"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Recepcionista> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(recepcionista: Recepcionista) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(recepcionista)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, recepcionista: Recepcionista) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(recepcionista)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(recepcionista: Recepcionista) {
        val id = requireNotNull(recepcionista.id) { "Recepcionista sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}