package edu.front.psi.clin.api

import edu.front.psi.clin.model.EvolucaoClinica
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class EvolucaoClinicaRequest(
    val prontuarioId: Long,
    val psicologoId: Long,
    val agendaId: Long,
    val dataRegistro: String,
    val descricao: String?
)

class EvolucaoClinicaAPI(private val httpClient: HttpClient) {
    private val endPoint = "/evolucoes-clinicas"
    private val token get() = SessionManager.token

    suspend fun carregar(): List<EvolucaoClinica> {
        val response = httpClient.get(endPoint) {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
        return response.body()
    }

    suspend fun salvar(evolucao: EvolucaoClinica) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(evolucao.toRequest())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, evolucao: EvolucaoClinica) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(evolucao.toRequest())
        }
        response.lancarErroSeNecessario()
    }

    private fun EvolucaoClinica.toRequest() = EvolucaoClinicaRequest(
        prontuarioId = prontuarioId,
        psicologoId = psicologoId,
        agendaId = agendaId,
        dataRegistro = formatarDataRegistro(dataRegistro),
        descricao = descricao
    )

    suspend fun apagar(evolucao: EvolucaoClinica) {
        val id = requireNotNull(evolucao.id) { "Evolução clínica sem identificador." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }

    private fun formatarDataRegistro(data: String): String {
        return when {
            data.matches(Regex("\\d{2}/\\d{2}/\\d{4}")) -> {
                val (dia, mes, ano) = data.split("/")
                "$ano-$mes-${dia}T00:00:00"
            }
            data.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> "${data}T00:00:00"
            data.matches(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2}(\\.\\d{1,9})?)?")) -> data
            else -> throw IllegalArgumentException("Data de registro da evolução clínica inválida.")
        }
    }
}
