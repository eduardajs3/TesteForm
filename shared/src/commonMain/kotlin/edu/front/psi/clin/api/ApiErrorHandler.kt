package edu.front.psi.clin.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

class BackendApiException(
    val statusCode: Int,
    message: String
) : Exception(message)

internal suspend fun HttpResponse.lancarErroSeNecessario() {
    if (status.isSuccess()) return

    throw BackendApiException(
        statusCode = status.value,
        message = mensagemDeErroApi(bodyAsText())
    )
}

internal fun mensagemDeErroApi(body: String): String {
    val corpo = body.trim()
    if (corpo.isEmpty()) return MENSAGEM_ERRO_PADRAO

    val objeto = runCatching { Json.parseToJsonElement(corpo) }
        .getOrNull() as? JsonObject

    val mensagemErro = (objeto?.get("erro") as? JsonPrimitive)
        ?.contentOrNull
        ?.takeIf(String::isNotBlank)
    if (mensagemErro != null) return mensagemErro

    val mensagensValidacao = objeto
        ?.filterKeys { it.lowercase() !in CAMPOS_METADATA_ERRO }
        ?.values
        ?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull?.takeIf(String::isNotBlank) }
        .orEmpty()
    if (mensagensValidacao.isNotEmpty()) return mensagensValidacao.joinToString("\n")

    if (objeto == null && !corpo.startsWith("{") && !corpo.startsWith("[")) {
        return corpo
    }

    return MENSAGEM_ERRO_PADRAO
}

fun Throwable.mensagemParaUsuario(mensagemGenerica: String): String {
    if (this is BackendApiException) {
        return message?.takeIf(String::isNotBlank) ?: mensagemGenerica
    }

    return message
        ?.takeIf(String::isNotBlank)
        ?.let { "$mensagemGenerica: $it" }
        ?: mensagemGenerica
}

private const val MENSAGEM_ERRO_PADRAO =
    "Não foi possível concluir a solicitação. Tente novamente."

private val CAMPOS_METADATA_ERRO = setOf(
    "timestamp",
    "status",
    "error",
    "path",
    "trace",
    "type",
    "title",
    "detail",
    "instance"
)
