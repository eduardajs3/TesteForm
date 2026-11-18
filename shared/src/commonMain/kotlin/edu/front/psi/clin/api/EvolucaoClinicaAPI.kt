package edu.front.psi.clin.api

import edu.front.psi.clin.model.EvolucaoClinica
import edu.front.psi.clin.utils.formatarDataBrasileiraOuIsoParaDataHoraIso
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

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
            setBody(evolucao.normalizada())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, evolucao: EvolucaoClinica) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(evolucao.normalizada())
        }
        response.lancarErroSeNecessario()
    }

    private fun EvolucaoClinica.normalizada() = copy(
        dataRegistro = formatarDataBrasileiraOuIsoParaDataHoraIso(
            dataRegistro,
            "Data de registro da evolução clínica inválida."
        )
    )

    suspend fun apagar(evolucao: EvolucaoClinica) {
        val id = requireNotNull(evolucao.id) { "Evolução clínica sem identificador." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }

}
 