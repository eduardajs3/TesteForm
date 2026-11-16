package edu.front.psi.clin.api

import edu.front.psi.clin.model.Funcionario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class FuncionarioAtualizacaoRequest(
    val id: Long? = null,
    val nome: String? = null,
    val cpf: String? = null,
    val email: String? = null,
    val telefone: String? = null,
    val ativo: Boolean? = true,
    val role: String? = null
)

class FuncionarioAPI(private val httpClient: HttpClient) {
    private val endPoint = "/funcionarios"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Funcionario> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Funcionario>>()

        println("GET Funcionários Carregados: $lista")
        return lista
    }

    suspend fun salvar(funcionario: Funcionario) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(funcionario)
        }
        validarResposta(response, "Salvar funcionário")
        println("Status do Salvar Funcionário: ${response.status}")
    }

    suspend fun atualizar(id: Long, funcionario: Funcionario) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(
                FuncionarioAtualizacaoRequest(
                    id = funcionario.id,
                    nome = funcionario.nome,
                    cpf = funcionario.cpf,
                    email = funcionario.email,
                    telefone = funcionario.telefone,
                    ativo = funcionario.ativo,
                    role = funcionario.role
                )
            )
        }
        validarResposta(response, "Atualizar funcionário")
        println("Status do Atualizar Funcionário: ${response.status}")
    }

    suspend fun alterarSenha(id: Long, novaSenha: String) {
        val response = httpClient.patch("$endPoint/$id/senha") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(AlterarSenhaRequest(novaSenha))
        }
        validarResposta(response, "Alterar senha do funcionário")
    }

    suspend fun apagar(funcionario: Funcionario) {
        val id = requireNotNull(funcionario.id) { "Funcionário sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        validarResposta(response, "Excluir funcionário")
        println("Status do Apagar Funcionário: ${response.status}")
    }

    private suspend fun validarResposta(response: HttpResponse, operacao: String) {
        if (response.status.value !in 200..299) {
            val detalhe = response.bodyAsText()
            throw IllegalStateException("$operacao falhou (${response.status}): $detalhe")
        }
    }
}
