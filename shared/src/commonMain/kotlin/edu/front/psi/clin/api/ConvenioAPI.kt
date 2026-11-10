// shared/src/commonMain/kotlin/edu/front/psi/clin/api/ConvenioAPI.kt
package edu.front.psi.clin.api

import edu.front.psi.clin.model.Convenio
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ConvenioAPI(private val httpClient: HttpClient) {
    private val endPoint = "http://localhost:8080/convenios"
    private val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBwc2ljbGluLmxvY2FsIiwiaWF0IjoxNzk0MjcxMjE5LCJleHAiOjE3OTQzNTc2MTl9.4iCfRcgUsdG3cUxwWfrEOJJ6Nh7n_7pQEs3_KiE8KVQ"

    suspend fun carregar(): List<Convenio> {
        val lista = httpClient.get(endPoint) {
            bearerAuth(token)
        }.body<List<Convenio>>()
        
        println("GET Carregar Lista: $lista")
        return lista
    }

    suspend fun salvar(convenio: Convenio) {
        val response = httpClient.post(endPoint) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(convenio)
        }
        println("Status do Salvar: ${response.status}")
    }

    suspend fun atualizar(id: Long, convenio: Convenio) {
        val response = httpClient.put("$endPoint/$id") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(convenio)
        }
        println("Status do Atualizar: ${response.status}")
    }

    suspend fun apagar(convenio: Convenio) {
        val response = httpClient.delete("$endPoint/${convenio.id}") {
            bearerAuth(token)
        }
        println("Status do Apagar: ${response.status}")
    }
}