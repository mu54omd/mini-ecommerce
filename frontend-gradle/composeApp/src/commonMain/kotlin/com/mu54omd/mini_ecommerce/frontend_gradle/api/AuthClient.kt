package com.mu54omd.mini_ecommerce.frontend_gradle.api

import com.mu54omd.mini_ecommerce.frontend_gradle.models.AuthRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.models.JwtResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.getTokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthClient(private val client: HttpClient, private val baseUrl: String) {

    private val tokenStorage = getTokenStorage()
    suspend fun login(username: String, password: String): JwtResponse {
        val response: HttpResponse = client.post("$baseUrl/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(username, password))
        }

        if (response.status == HttpStatusCode.OK) {
            val jwt = response.body<JwtResponse>().token
            tokenStorage.saveToken(jwt)
            return response.body()
        } else {
            throw Exception("Login failed: ${response.status}")
        }
    }

    suspend fun logout(){
        tokenStorage.clearToken()
    }

    suspend fun getStoredToken(): String? = tokenStorage.getToken()
}