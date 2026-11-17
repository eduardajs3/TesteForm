package edu.front.psi.clin.api

import edu.front.psi.clin.model.BloqueioAgenda
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class BloqueioAgendaAPI(private val httpClient: HttpClient) {
    private val endPoint = "/bloqueios-agenda"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<BloqueioAgenda> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(bloqueio: BloqueioAgenda) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(bloqueio)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, bloqueio: BloqueioAgenda) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(bloqueio)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(bloqueio: BloqueioAgenda) {
        val id = requireNotNull(bloqueio.id) { "Bloqueio sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}
