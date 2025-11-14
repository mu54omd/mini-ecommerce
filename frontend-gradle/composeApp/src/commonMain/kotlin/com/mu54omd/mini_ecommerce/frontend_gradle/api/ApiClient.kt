package com.mu54omd.mini_ecommerce.frontend_gradle.api

import com.mu54omd.mini_ecommerce.frontend_gradle.storage.SessionManager
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.Constants.BASE_URL_API_CLIENT
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApiClient(
    private val client: HttpClient,
) : KoinComponent {

    private val sessionManager: SessionManager by inject()
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
            400 -> {
                // Validation / Bad Request
                try {
                    val jsonObj = json.parseToJsonElement(text).jsonObject
                    val message = jsonObj["message"]?.jsonPrimitive?.content ?: "Validation failed"
                    val fields = jsonObj["fields"]?.jsonObject?.mapValues { it.value.jsonPrimitive.content }
                    ApiResult.ValidationError(message = message, fields = fields)
                } catch (e: Exception) {
                    ApiResult.Error(message = "Validation failed", code = 400)
                }
            }
            401 -> {
                try {
                    val jsonObj = json.parseToJsonElement(text).jsonObject
                    val message = jsonObj["message"]?.jsonPrimitive?.content ?: "Unauthorized Request"
                    ApiResult.Unauthorized(message = message)
                } catch (e: Exception) {
                    ApiResult.Unauthorized(message = "Unauthorized")
                }
            }
            403 -> {
                try {
                    val jsonObj = json.parseToJsonElement(text).jsonObject
                    val message = jsonObj["message"]?.jsonPrimitive?.content ?: "Forbidden Request"
                    ApiResult.Forbidden(message = message)
                } catch (e: Exception) {
                    ApiResult.Forbidden(message = "Forbidden")
                }
            }
            else -> {
                try {
                    val jsonObj = json.parseToJsonElement(text).jsonObject
                    val message = jsonObj["message"]?.jsonPrimitive?.content ?: "HTTP ${response.status.value}"
                    ApiResult.Error(message = message, code = response.status.value)
                } catch (e: Exception) {
                    ApiResult.Error(message = "HTTP ${response.status.value}: $text", code = response.status.value)
                }
            }
        }
    }

    private fun Throwable.toApiError(): ApiResult.Error =
        ApiResult.Error(message = this.message ?: "Unexpected error occurred")
    // endregion

    // region GET
    suspend fun <R> get(path: String, deserializer: KSerializer<R>): ApiResult<R> {
        return try {
            val response = client.get("$BASE_URL_API_CLIENT$path") { authHeader() }
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
            val response = client.post("$BASE_URL_API_CLIENT$path") {
                authHeader()
                contentType(ContentType.Application.Json)
                setBody(json.encodeToString(reqSerializer, body))
            }
            handleResponse(response, resSerializer)
        } catch (e: Exception) {
            e.toApiError()
        }
    }

    suspend fun <R> postMultipart(
        path: String,
        body: MultiPartFormDataContent,
        resSerializer: KSerializer<R>
    ): ApiResult<R> {
        return try {
            val response = client.post("$BASE_URL_API_CLIENT$path") {
                authHeader()
                setBody(body)
            }
            handleResponse(response, resSerializer)
        } catch (e: Exception) {
            e.toApiError()
        }
    }

    suspend inline fun <reified T, reified R> postMultipart(path: String, body: T): ApiResult<R> =
        postMultipart(path, body as MultiPartFormDataContent, serializer())

    suspend inline fun <reified T, reified R> post(path: String, body: T): ApiResult<R> =
        post(path, body, serializer(), serializer())
    // endregion

    // region PUT
    suspend fun <T, R> put(path: String, body: T, reqSerializer: KSerializer<T>, resSerializer: KSerializer<R>): ApiResult<R> {
        return try {
            val response = client.put("$BASE_URL_API_CLIENT$path") {
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

    // region PATCH
    suspend fun patch(path: String): ApiResult<Unit> {
        return try {
            val response = client.patch("$BASE_URL_API_CLIENT$path") { authHeader() }
            when (response.status.value) {
                in 200..299 -> ApiResult.Success(Unit)
                401 -> ApiResult.Unauthorized()
                else -> ApiResult.Error("Patch failed: ${response.status}")
            }
        } catch (e: Exception) {
            e.toApiError()
        }
    }
    // endregion

    // region DELETE
    suspend fun delete(path: String): ApiResult<Unit> {
        return try {
            val response = client.delete("$BASE_URL_API_CLIENT$path") { authHeader() }
            when (response.status.value) {
                in 200..299 -> ApiResult.Success(Unit)
                401 -> ApiResult.Unauthorized()
                else -> ApiResult.Error("Delete failed: ${response.status}")
            }
        } catch (e: Exception) {
            e.toApiError()
        }
    }
    // endregion
}