package edu.front.psi.clin.api

import edu.front.psi.clin.model.SalaAtendimento
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SalaAtendimentoAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/salas-atendimento" // Ajuste para o endpoint correto do seu backend
    private val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBwc2ljbGluLmxvY2FsIiwiaWF0IjoxNzk0MjczODI3LCJleHAiOjE3OTQzNjAyMjd9.E6L-d143uQVWg5B41GVkwd93APXYMmQWyOGS3W9_ipE" // Use o seu token ativo

    suspend fun carregar(): List<SalaAtendimento> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<SalaAtendimento>>()
        
        println("GET Salas Carregadas: $lista")
        return lista
    }

    suspend fun salvar(sala: SalaAtendimento) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(sala)
        }
        println("Status do Salvar Sala: ${response.status}")
    }

    suspend fun atualizar(id: Long, sala: SalaAtendimento) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(sala)
        }
        println("Status do Atualizar Sala: ${response.status}")
    }

    suspend fun apagar(sala: SalaAtendimento) {
        val response = httpClient.delete("$endPoint/${sala.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Sala: ${response.status}")
    }
}