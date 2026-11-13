package edu.front.psi.clin.api

import edu.front.psi.clin.model.Psicologo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PsicologoAPI(private val httpClient: HttpClient) {
    private val endPoint = "https://psiclin-backend.onrender.com/psicologos"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Psicologo> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Psicologo>>()
        
        println("GET Psicólogos Carregados: $lista")
        return lista
    }

    suspend fun salvar(psicologo: Psicologo) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(psicologo)
        }
        println("Status do Salvar Psicólogo: ${response.status}")
    }

    suspend fun atualizar(id: Long, psicologo: Psicologo) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(psicologo)
        }
        println("Status do Atualizar Psicólogo: ${response.status}")
    }

    suspend fun apagar(psicologo: Psicologo) {
        val response = httpClient.delete("$endPoint/${psicologo.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Psicólogo: ${response.status}")
    }
}