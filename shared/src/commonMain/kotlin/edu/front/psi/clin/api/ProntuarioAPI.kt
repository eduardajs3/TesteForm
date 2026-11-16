package edu.front.psi.clin.api

import edu.front.psi.clin.model.Prontuario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class ProntuarioRequest(
    val id: Long? = null,
    val pacienteId: Long,
    val dataAbertura: String? = null,
    val queixaPrincipal: String,
    val status: String
)

class ProntuarioAPI(private val httpClient: HttpClient) {
    private val endPoint = "/prontuarios"
    private val token get() = SessionManager.token

    suspend fun carregar(): List<Prontuario> {
        val response = httpClient.get(endPoint) {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
        return response.body()
    }

    suspend fun salvar(prontuario: Prontuario) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(prontuario.toRequest())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, prontuario: Prontuario) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(prontuario.toRequest())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(prontuario: Prontuario) {
        val id = requireNotNull(prontuario.id) { "Prontuário sem identificador." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }

    private fun Prontuario.toRequest() = ProntuarioRequest(
        id = id,
        pacienteId = requireNotNull(pacienteId) { "Selecione um paciente para o prontuário." },
        dataAbertura = dataAbertura?.let(::formatarDataAbertura),
        queixaPrincipal = queixaPrincipal,
        status = status?.uppercase() ?: "ATIVO"
    )

    private fun formatarDataAbertura(data: String): String {
        return when {
            data.matches(Regex("\\d{2}/\\d{2}/\\d{4}")) -> {
                val (dia, mes, ano) = data.split("/")
                "$ano-$mes-${dia}T00:00:00"
            }
            data.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> "${data}T00:00:00"
            data.matches(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2}(\\.\\d{1,9})?)?")) -> data
            else -> throw IllegalArgumentException("Data de abertura do prontuário inválida.")
        }
    }
}
