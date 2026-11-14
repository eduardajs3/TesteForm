
package edu.front.psi.clin.api

import edu.front.psi.clin.model.Agenda
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AgendaAPI(private val httpClient: HttpClient) {
    private val endPoint = "/agendas"
    private val token get() = SessionManager.token

    suspend fun carregar(): List<Agenda> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Agenda>>()
        println("GET Agendas Carregadas: $lista")
        return lista
    }

    suspend fun salvar(agenda: Agenda) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(agenda)
        }
        validarResposta(response, "Salvar agendamento")
    }

    suspend fun atualizar(id: Long, agenda: Agenda) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(agenda)
        }
        validarResposta(response, "Atualizar agendamento")
    }

    suspend fun apagar(agenda: Agenda) {
        val response = httpClient.delete("$endPoint/${agenda.id}") {
            bearerAuth(token)
        }
        validarResposta(response, "Excluir agendamento")
    }

    private suspend fun validarResposta(response: HttpResponse, operacao: String) {
        if (response.status.value !in 200..299) {
            val detalhe = response.bodyAsText()
            throw IllegalStateException("$operacao falhou (${response.status}): $detalhe")
        }
    }
}