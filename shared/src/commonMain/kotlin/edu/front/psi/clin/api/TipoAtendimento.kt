package edu.front.psi.clin.api

import edu.front.psi.clin.model.TipoAtendimento
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TipoAtendimentoAPI(private val httpClient: HttpClient) {
    private val endPoint = "/tipos-atendimento"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<TipoAtendimento> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(tipo: TipoAtendimento) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(tipo)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, tipo: TipoAtendimento) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(tipo)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(tipo: TipoAtendimento) {
        val id = requireNotNull(tipo.id) { "Tipo de atendimento sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id/inativar") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}