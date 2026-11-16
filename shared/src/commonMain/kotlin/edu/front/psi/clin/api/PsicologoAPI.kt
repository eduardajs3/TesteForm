package edu.front.psi.clin.api

import edu.front.psi.clin.model.Psicologo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class PsicologoAtualizacaoRequest(
    val id: Long? = null,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String,
    val ativo: Boolean,
    val crp: String,
    val especialidade: String
)

class PsicologoAPI(private val httpClient: HttpClient) {
    private val endPoint = "/psicologos"
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
            setBody(
                PsicologoAtualizacaoRequest(
                    id = psicologo.id,
                    nome = psicologo.nome,
                    cpf = psicologo.cpf,
                    email = psicologo.email,
                    telefone = psicologo.telefone,
                    ativo = psicologo.ativo,
                    crp = psicologo.crp,
                    especialidade = psicologo.especialidade
                )
            )
        }
        println("Status do Atualizar Psicólogo: ${response.status}")
    }

    suspend fun alterarSenha(id: Long, novaSenha: String) {
        httpClient.patch("$endPoint/$id/senha") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(AlterarSenhaRequest(novaSenha))
        }
    }

    suspend fun apagar(psicologo: Psicologo) {
        val response = httpClient.delete("$endPoint/${psicologo.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar Psicólogo: ${response.status}")
    }
}