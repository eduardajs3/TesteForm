package edu.front.psi.clin.api

import edu.front.psi.clin.model.Psicologo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PsicologoAPI(private val httpClient: HttpClient) {
    private val endPoint = "/psicologos"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Psicologo> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(psicologo: Psicologo) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(psicologo)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, psicologo: Psicologo) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(psicologo)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(psicologo: Psicologo) {
        val id = requireNotNull(psicologo.id) { "Psicólogo sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}