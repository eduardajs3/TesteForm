package edu.front.psi.clin.api

import edu.front.psi.clin.model.Paciente
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PacienteAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/pacientes" 
    private val token
        get() = SessionManager.token
    suspend fun carregar(): List<Paciente> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Paciente>>()
        
        println("GET Pacientes Carregados: $lista")
        return lista
    }

    suspend fun salvar(paciente: Paciente) {

        println("TOKEN: ${SessionManager.token}")
        println("PACIENTE ENVIADO: $paciente")
        val response = httpClient.post(endPoint) {
            header("Authorization", "Bearer ${SessionManager.token}")
            contentType(ContentType.Application.Json)
            setBody(paciente)
        }

        println("Status do Salvar Paciente: ${response.status}")
        println("Resposta Detalhada: ${response.bodyAsText()}")
    }
    suspend fun atualizar(id: Long, paciente: Paciente) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(paciente)
        }
        println("Status do Atualizar Paciente: ${response.status}")
    }

    suspend fun apagar(paciente: Paciente) {
        val response = httpClient.delete("$endPoint/${paciente.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Paciente: ${response.status}")
    }
}