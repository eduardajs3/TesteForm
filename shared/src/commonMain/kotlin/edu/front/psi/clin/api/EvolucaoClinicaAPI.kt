package edu.front.psi.clin.api

import edu.front.psi.clin.model.EvolucaoClinica
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class EvolucaoClinicaAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/evolucoes-clinicas"

    suspend fun carregar(): List<EvolucaoClinica> =
        httpClient.get(endPoint) {
            bearerAuth(SessionManager.requireToken())
        }.body()

    suspend fun salvar(evolucao: EvolucaoClinica) {
        httpClient.post(endPoint) {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(evolucao)
        }
    }

    suspend fun atualizar(id: Long, evolucao: EvolucaoClinica) {
        httpClient.put("$endPoint/$id") {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(evolucao)
        }
    }

    suspend fun apagar(evolucao: EvolucaoClinica) {
        httpClient.delete("$endPoint/${evolucao.id}") {
            bearerAuth(SessionManager.requireToken())
        }
    }
}
