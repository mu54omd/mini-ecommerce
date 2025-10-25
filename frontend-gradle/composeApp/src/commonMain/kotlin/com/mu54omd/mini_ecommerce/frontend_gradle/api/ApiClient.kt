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
import io.ktor.client.statement.HttpResponse
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
import org.koin.core.component.inject

class ApiClient(
    private val client: HttpClient,
) : KoinComponent {

    private val sessionManager: SessionManager by inject()
    private val baseUrl = "http://192.168.1.101:5050/api"
    private val json = Json { ignoreUnknownKeys = true }


    // region Auth Header
    private suspend fun HttpRequestBuilder.authHeader() {
        sessionManager.getToken()?.let { token ->
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
    // endregion

    // region Internal Helpers
    private suspend fun <R> handleResponse(response: HttpResponse, deserializer: KSerializer<R>): ApiResult<R> {
        val text = response.bodyAsText()

        return when (response.status.value) {
            in 200..299 -> {
                val data = json.decodeFromString(deserializer, text)
                ApiResult.Success(data)
            }
            401 -> ApiResult.Unauthorized("Unauthorized request")
            403 -> ApiResult.Forbidden("Forbidden request")
            else -> ApiResult.Error( message = "HTTP ${response.status.value}: $text", code = response.status.value)
        }
    }

    private fun Throwable.toApiError(): ApiResult.Error =
        ApiResult.Error(message = this.message ?: "Unexpected error occurred")
    // endregion

    // region GET
    suspend fun <R> get(path: String, deserializer: KSerializer<R>): ApiResult<R> {
        return try {
            val response = client.get("$baseUrl$path") { authHeader() }
            handleResponse(response, deserializer)
        } catch (e: Exception) {
            e.toApiError()
        }
    }
    suspend inline fun <reified R> get(path: String): ApiResult<R> =
        get(path, serializer())
    suspend inline fun <reified R> getList(path: String): ApiResult<List<R>> =
        get(path, ListSerializer(serializer()))
    // endregion

    // region POST
    suspend fun <T, R> post(path: String, body: T, reqSerializer: KSerializer<T>, resSerializer: KSerializer<R>): ApiResult<R> {
        return try {
            val response = client.post("$baseUrl$path") {
                authHeader()
                contentType(ContentType.Application.Json)
                setBody(json.encodeToString(reqSerializer, body))
            }
            handleResponse(response, resSerializer)
        } catch (e: Exception) {
            e.toApiError()
        }
    }

    suspend inline fun <reified T, reified R> post(path: String, body: T): ApiResult<R> =
        post(path, body, serializer(), serializer())
    // endregion

    // region PUT
    suspend fun <T, R> put(path: String, body: T, reqSerializer: KSerializer<T>, resSerializer: KSerializer<R>): ApiResult<R> {
        return try {
            val response = client.put("$baseUrl$path") {
                authHeader()
                contentType(ContentType.Application.Json)
                setBody(json.encodeToString(reqSerializer, body))
            }
            handleResponse(response, resSerializer)
        } catch (e: Exception) {
            e.toApiError()
        }
    }

    suspend inline fun <reified T, reified R> put(path: String, body: T): ApiResult<R> =
        put(path, body, serializer(), serializer())
    // endregion

    // region DELETE
    suspend fun delete(path: String): ApiResult<Unit> {
        return try {
            val response = client.delete("$baseUrl$path") { authHeader() }
            if (response.status.value in 200..299)
                ApiResult.Success(Unit)
            else if (response.status.value == 401)
                ApiResult.Unauthorized()
            else
                ApiResult.Error("Delete failed: ${response.status}")
        } catch (e: Exception) {
            e.toApiError()
        }
    }
    // endregion
}