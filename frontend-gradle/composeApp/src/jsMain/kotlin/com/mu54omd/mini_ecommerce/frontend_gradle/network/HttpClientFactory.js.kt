package com.mu54omd.mini_ecommerce.frontend_gradle.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@OptIn(ExperimentalWasmJsInterop::class)
fun getTokenFromLocalStorage(): String? = js("localStorage.getItem('jwt')")

actual fun createHttpClient(): HttpClient = HttpClient(Js) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(DefaultRequest) {
        val token = getTokenFromLocalStorage()
        if (!token.isNullOrBlank()) {
            header("Authorization", "Bearer $token")
        }
    }
}