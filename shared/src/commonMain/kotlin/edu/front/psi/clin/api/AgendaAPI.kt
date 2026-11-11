// shared/src/commonMain/kotlin/edu/front/psi/clin/api/AgendaAPI.kt
package edu.front.psi.clin.api

import edu.front.psi.clin.model.Agenda
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AgendaAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/agendas"
    private val token get() = SessionManager.token

    suspend fun carregar(): List<Agenda> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Agenda>>()
        println("GET Agendas Carregadas: $lista")
        return lista
    }

    suspend fun salvar(agenda: Agenda) {

        println("TOKEN: ${SessionManager.token}")
        println("AGENDA ENVIADA: $agenda")

        val response = httpClient.post(endPoint) {
            header("Authorization", "Bearer ${SessionManager.token}")
            contentType(ContentType.Application.Json)
            setBody(agenda)
        }

        println("Status do Salvar Agenda: ${response.status}")
        println("Resposta Detalhada: ${response.bodyAsText()}")
    }

    suspend fun atualizar(id: Long, agenda: Agenda) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(agenda)
        }
        println("Status do Atualizar Agenda: ${response.status}")
    }

    suspend fun apagar(agenda: Agenda) {
        val response = httpClient.delete("$endPoint/${agenda.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Agenda: ${response.status}")
    }
}