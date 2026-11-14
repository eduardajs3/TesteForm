package edu.front.psi.clin.api

import edu.front.psi.clin.model.Pagamento
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PagamentoAPI(private val httpClient: HttpClient) {
    private val endPoint = "/pagamentos"
    private val token get() = SessionManager.token

    suspend fun carregar(): List<Pagamento> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Pagamento>>()
        println("GET Pagamentos Carregados: $lista")
        return lista
    }

    suspend fun salvar(pagamento: Pagamento) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(pagamento)
        }
        println("Status do Salvar Pagamento: ${response.status}")
        println("Resposta Detalhada: ${response.bodyAsText()}")
    }

    suspend fun atualizar(id: Long, pagamento: Pagamento) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(pagamento)
        }
        println("Status do Atualizar Pagamento: ${response.status}")
    }

    suspend fun apagar(pagamento: Pagamento) {
        val response = httpClient.delete("$endPoint/${pagamento.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Pagamento: ${response.status}")
    }
}