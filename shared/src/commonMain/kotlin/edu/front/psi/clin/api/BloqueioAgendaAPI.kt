package edu.front.psi.clin.api

import edu.front.psi.clin.model.BloqueioAgenda
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class BloqueioAgendaAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/bloqueios-agenda"

    suspend fun carregar(): List<BloqueioAgenda> =
        httpClient.get(endPoint) {
            bearerAuth(SessionManager.requireToken())
        }.body()

    suspend fun salvar(bloqueio: BloqueioAgenda) {
        httpClient.post(endPoint) {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(bloqueio)
        }
    }

    suspend fun atualizar(id: Long, bloqueio: BloqueioAgenda) {
        httpClient.put("$endPoint/$id") {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(bloqueio)
        }
    }

    suspend fun apagar(bloqueio: BloqueioAgenda) {
        httpClient.delete("$endPoint/${bloqueio.id}") {
            bearerAuth(SessionManager.requireToken())
        }
    }
}
