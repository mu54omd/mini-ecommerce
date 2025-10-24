package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.AuthRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.JwtResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.SessionManager
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class AuthRepository(private val api: ApiClient, private val sessionManager: SessionManager) {
    suspend fun login(username: String, password: String): JwtResponse {
        val response = api.post<AuthRequest, JwtResponse>("/auth/login", AuthRequest(username, password))
        val jwt = response.token
        sessionManager.saveToken(jwt)
        return response
    }

    suspend fun logout(){
        sessionManager.clearToken()
    }

    suspend fun getStoredToken(): String? = sessionManager.getToken()
}