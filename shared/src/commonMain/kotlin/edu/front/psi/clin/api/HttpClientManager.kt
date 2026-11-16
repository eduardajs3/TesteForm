package edu.front.psi.clin.api
import io.ktor.client.HttpClient

expect fun httpClientManager() : HttpClient
