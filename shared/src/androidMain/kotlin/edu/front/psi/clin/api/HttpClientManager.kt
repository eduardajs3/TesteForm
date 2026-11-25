package edu.front.psi.clin.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun httpClientManager(): HttpClient {
    return HttpClient(Android) {
        expectSuccess = false

        HttpResponseValidator {
            validateResponse { response ->
                response.lancarErroSeNecessario()
            }
        }

        install(DefaultRequest) {
            url(ApiConfig.BASE_URL)
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    prettyPrint = true
                }
            )
        }
    }
}