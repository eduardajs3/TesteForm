package edu.front.psi.clin.api

import edu.front.psi.clin.model.SalaAtendimento
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SalaAtendimentoAPI(private val httpClient: HttpClient) {
    private val endPoint = "/salas-atendimento"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<SalaAtendimento> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(sala: SalaAtendimento) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(sala)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, sala: SalaAtendimento) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(sala)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(sala: SalaAtendimento) {
        val id = requireNotNull(sala.id) { "Sala de atendimento sem identificador não pode ser excluída." }
        val response = httpClient.delete("$endPoint/$id/inativar") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}