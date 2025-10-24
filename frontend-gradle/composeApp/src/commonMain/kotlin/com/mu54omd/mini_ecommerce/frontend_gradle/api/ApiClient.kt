package com.mu54omd.mini_ecommerce.frontend_gradle.api

import com.mu54omd.mini_ecommerce.frontend_gradle.storage.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
/**
 * Universal API Client — works on JVM, Android, iOS, Web
 * Handles JWT, CORS, and error responses gracefully.
 */
class ApiClient(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : KoinComponent {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
//    private val baseUrl = "http://localhost:5050/api"
    private val baseUrl = "http://192.168.1.101:5050/api"

    // Add auth header if JWT exists
    private suspend fun HttpRequestBuilder.authHeader() {
        sessionManager.getToken()?.let { token ->
            println("token: $token")
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    // --- Generic request helpers ---

    suspend fun <R> get(path: String, deserializer: KSerializer<R>): R {
        val response = client.get("$baseUrl$path") { authHeader() }
        val text = response.bodyAsText()
        if (!response.status.isSuccess()) throw ApiException("GET $path -> ${response.status}: $text")
        return json.decodeFromString(deserializer, text)
    }

    suspend fun <T, R> post(path: String, body: T, reqSerializer: KSerializer<T>, resSerializer: KSerializer<R>): R {
        val response = client.post("$baseUrl$path") {
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(reqSerializer, body))
            authHeader()
        }
        val text = response.bodyAsText()
        if (!response.status.isSuccess()) throw ApiException("POST $path -> ${response.status}: $text")
        return json.decodeFromString(resSerializer, text)
    }

    suspend fun <T, R> put(path: String, body: T, reqSerializer: KSerializer<T>, resSerializer: KSerializer<R>): R {
        val response = client.put("$baseUrl$path") {
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(reqSerializer, body))
            authHeader()
        }
        val text = response.bodyAsText()
        if (!response.status.isSuccess()) throw ApiException("PUT $path -> ${response.status}: $text")
        return json.decodeFromString(resSerializer, text)
    }

    suspend fun delete(path: String): Boolean {
        val response = client.delete("$baseUrl$path") { authHeader() }
        if (!response.status.isSuccess()) throw ApiException("DELETE $path -> ${response.status}: ${response.bodyAsText()}")
        return true
    }

    // --- Convenience inline reified shortcuts ---

    suspend inline fun <reified R> get(path: String): R = get(path, serializer())
    suspend inline fun <reified T, reified R> post(path: String, body: T): R =
        post(path, body, serializer(), serializer())
    suspend inline fun <reified T, reified R> put(path: String, body: T): R =
        put(path, body, serializer(), serializer())

    // --- Example helper for list responses ---
    suspend inline fun <reified R> getList(path: String): List<R> =
        get(path, ListSerializer(serializer()))
}

// --- Exceptions ---
class ApiException(message: String) : Exception(message)


