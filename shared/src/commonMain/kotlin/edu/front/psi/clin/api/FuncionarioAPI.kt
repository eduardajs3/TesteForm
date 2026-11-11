package edu.front.psi.clin.api

import edu.front.psi.clin.model.Funcionario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class FuncionarioAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/funcionarios"

    suspend fun carregar(): List<Funcionario> =
        httpClient.get(endPoint) {
            bearerAuth(SessionManager.requireToken())
        }.body()

    suspend fun salvar(funcionario: Funcionario) {
        httpClient.post(endPoint) {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(funcionario)
        }
    }

    suspend fun atualizar(id: Long, funcionario: Funcionario) {
        httpClient.put("$endPoint/$id") {
            bearerAuth(SessionManager.requireToken())
            contentType(ContentType.Application.Json)
            setBody(funcionario)
        }
    }

    suspend fun apagar(funcionario: Funcionario) {
        httpClient.delete("$endPoint/${funcionario.id}") {
            bearerAuth(SessionManager.requireToken())
        }
    }
}
