package com.mu54omd.mini_ecommerce.frontend_gradle.api

import com.mu54omd.mini_ecommerce.frontend_gradle.storage.getTokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

actual fun createHttpClient(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(DefaultRequest) {
        val storage = getTokenStorage()
        val token = runBlocking { storage.getToken() }
        if (!token.isNullOrBlank()) {
            header("Authorization", "Bearer $token")
        }
    }
}