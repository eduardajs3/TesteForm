package edu.front.psi.clin.api

import edu.front.psi.clin.model.TipoAtendimento
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TipoAtendimentoAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/tipos-atendimento" 
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<TipoAtendimento> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<TipoAtendimento>>()
        
        println("GET Tipos de Atendimento Carregados: $lista")
        return lista
    }

    suspend fun salvar(tipo: TipoAtendimento) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(tipo)
        }
        println("Status do Salvar Tipo de Atendimento: ${response.status}")
    }

    suspend fun atualizar(id: Long, tipo: TipoAtendimento) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(tipo)
        }
        println("Status do Atualizar Tipo de Atendimento: ${response.status}")
    }

    suspend fun apagar(tipo: TipoAtendimento) {
        val response = httpClient.delete("$endPoint/${tipo.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Tipo de Atendimento: ${response.status}")
    }
}