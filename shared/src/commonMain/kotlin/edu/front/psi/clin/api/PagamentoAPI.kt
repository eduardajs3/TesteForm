package edu.front.psi.clin.api

import edu.front.psi.clin.model.Pagamento
import edu.front.psi.clin.utils.formatarDataBrasileiraOuIsoParaDataHoraIso
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PagamentoAPI(private val httpClient: HttpClient) {
    private val endPoint = "/pagamentos"
    private val token
        get() = SessionManager.token

    suspend fun carregar(): List<Pagamento> =
        httpClient.get(endPoint) {
            bearerAuth(token)
        }.body()

    suspend fun salvar(pagamento: Pagamento) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(pagamento.normalizado())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun atualizar(id: Long, pagamento: Pagamento) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(pagamento.normalizado())
        }
        response.lancarErroSeNecessario()
    }

    suspend fun apagar(pagamento: Pagamento) {
        val id = requireNotNull(pagamento.id) { "Pagamento sem identificador não pode ser excluído." }
        val response = httpClient.delete("$endPoint/$id") {
            bearerAuth(token)
        }
        response.lancarErroSeNecessario()
    }

    private fun Pagamento.normalizado() = copy(
        dataPagamento = formatarDataBrasileiraOuIsoParaDataHoraIso(
            dataPagamento,
            "Data de pagamento inválida: use dd/MM/yyyy ou yyyy-MM-dd."
        )
    )
}