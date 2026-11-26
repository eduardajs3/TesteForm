package edu.front.psi.clin.api

import edu.front.psi.clin.model.Prontuario
import edu.front.psi.clin.utils.formatarDataBrasileiraOuIsoParaDataHoraIso
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProntuarioAPI(private val httpClient: HttpClient) {
    private val endPoint = "/prontuarios"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Prontuario> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(prontuario: Prontuario) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(prontuario.normalizado())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, prontuario: Prontuario) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(prontuario.normalizado())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(prontuario: Prontuario) {
        val id = requireNotNull(prontuario.id) { "Prontuário sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }

    private fun Prontuario.normalizado() = copy(
        dataAbertura = formatarDataBrasileiraOuIsoParaDataHoraIso(
            dataAbertura,
            "Data de abertura do prontuário inválida."
        )
    )
}