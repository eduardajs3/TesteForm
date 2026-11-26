package edu.front.psi.clin.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

class BackendApiException(
    val statusCode: Int,
    val mensagemAmigavel: String,
    val detalheTecnico: String
) : Exception(mensagemAmigavel)

internal suspend fun HttpResponse.lancarErroSeNecessario() {
    if (status.isSuccess()) return

    val request = call.request
    val authorization = request.headers[HttpHeaders.Authorization].orEmpty()
    val bearerPresente = authorization.startsWith("Bearer ", ignoreCase = true)
    val endpoint = "${request.method.value} ${request.url.encodedPath}"
    val autenticacao = if (bearerPresente) "Bearer enviado" else "Bearer ausente"
    val detalheApi = mensagemDeErroApi(bodyAsText())

    val mensagemAmigavel = when (status.value) {
        401 -> "Sua sessão expirou ou você não está autenticado. Faça login novamente."
        403 -> "Você não tem permissão para realizar esta ação."
        else -> detalheApi.takeIf { it != MENSAGEM_ERRO_PADRAO } ?: MENSAGEM_ERRO_PADRAO
    }

    val detalheTecnico = "$endpoint retornou HTTP ${status.value} ($autenticacao): $detalheApi"
    println("[API ERROR] $detalheTecnico")

    throw BackendApiException(
        statusCode = status.value,
        mensagemAmigavel = mensagemAmigavel,
        detalheTecnico = detalheTecnico
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
        return mensagemAmigavel.takeIf(String::isNotBlank) ?: mensagemGenerica
    }

    return mensagemGenerica
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