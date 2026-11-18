package edu.front.psi.clin.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun httpClientManager(): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = false

        HttpResponseValidator {
            validateResponse { response ->
                response.lancarErroSeNecessario()
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 60000
            socketTimeoutMillis = 60000
        }

        install(DefaultRequest) {
            url(ApiConfig.BASE_URL)
        }

        install(ContentNegotiation) {

            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }
            )
        }
    }
}