package edu.front.psi.clin.api

import edu.front.psi.clin.model.Funcionario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class FuncionarioAPI(private val httpClient: HttpClient) {
    private val endPoint = "/funcionarios"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Funcionario> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(funcionario: Funcionario) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(funcionario)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, funcionario: Funcionario) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(funcionario)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(funcionario: Funcionario) {
        val id = requireNotNull(funcionario.id) { "Funcionário sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}