package edu.front.psi.clin.api

import edu.front.psi.clin.model.HorarioDisponivel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class HorarioDisponivelAPI(private val httpClient: HttpClient) {
    private val endPoint = "/horarios-disponiveis"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<HorarioDisponivel> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(horario: HorarioDisponivel) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(horario)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, horario: HorarioDisponivel) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(horario)
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(horario: HorarioDisponivel) {
        val id = requireNotNull(horario.id) { "Horário disponível sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }
}