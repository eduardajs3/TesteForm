package edu.front.psi.clin.api

import edu.front.psi.clin.model.HorarioDisponivel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class HorarioDisponivelAPI(private val httpClient: HttpClient) {
    private val endPoint = "/horarios-disponiveis"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<HorarioDisponivel> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<HorarioDisponivel>>()
        
        println("GET Horários Disponíveis Carregados: $lista")
        return lista
    }

    suspend fun salvar(horario: HorarioDisponivel) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(horario)
        }
        println("Status do Salvar Horário Disponível: ${response.status}")
        println("Resposta Detalhada do Backend: ${response.bodyAsText()}")
    }

    suspend fun atualizar(id: Long, horario: HorarioDisponivel) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(horario)
        }
        println("Status do Atualizar Horário Disponível: ${response.status}")
    }

    suspend fun apagar(horario: HorarioDisponivel) {
        println("ID enviado para exclusão: ${horario.id}")

        val response = httpClient.delete("$endPoint/${horario.id}") {
            bearerAuth(token)
        }

        println("Status do Apagar Horário Disponível: ${response.status}")
        println("Resposta: ${response.bodyAsText()}")
    }
}