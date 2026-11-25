package edu.front.psi.clin.api

import edu.front.psi.clin.model.Agenda
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AgendaAPI(private val httpClient: HttpClient) {
    private val endPoint = "/agendas"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Agenda> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(agenda: Agenda) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(agenda)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, agenda: Agenda) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(agenda)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(agenda: Agenda) {
        val id = requireNotNull(agenda.id) { "Agendamento sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}