package edu.front.psi.clin.api

import edu.front.psi.clin.model.Recepcionista
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RecepcionistaAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/recepcionistas" // Ajuste conforme o endpoint exato do backend
    private val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBwc2ljbGluLmxvY2FsIiwiaWF0IjoxNzk0Mjc2NTY1LCJleHAiOjE3OTQzNjI5NjV9.aL-VS30bi4ZPUs74Idz7RTdd7wQYNtMc8vrIioyrbvk" // Token JWT válido/atualizado

    suspend fun carregar(): List<Recepcionista> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Recepcionista>>()
        
        println("GET Recepcionistas Carregados: $lista")
        return lista
    }

    suspend fun salvar(recepcionista: Recepcionista) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(recepcionista)
        }
        println("Status do Salvar Recepcionista: ${response.status}")
    }

    suspend fun atualizar(id: Long, recepcionista: Recepcionista) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(recepcionista)
        }
        println("Status do Atualizar Recepcionista: ${response.status}")
    }

    suspend fun apagar(recepcionista: Recepcionista) {
        val response = httpClient.delete("$endPoint/${recepcionista.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Recepcionista: ${response.status}")
    }
}