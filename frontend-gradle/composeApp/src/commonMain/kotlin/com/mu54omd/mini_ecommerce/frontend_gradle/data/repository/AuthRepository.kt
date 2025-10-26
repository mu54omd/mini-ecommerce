package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult.*
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.AuthRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.JwtResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.SessionManager
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState

class AuthRepository(
    private val api: ApiClient,
    private val sessionManager: SessionManager
) {
    suspend fun login(username: String, password: String): ApiResult<String> {
        var result = api.post<AuthRequest, JwtResponse>("/auth/login", AuthRequest(username, password))
            .map(
                onSuccess = { it.token },
                onAfterSuccess = { sessionManager.saveToken(it)}
            )
        if(result !is ApiResult.Success){
            sessionManager.clearToken()
            result = api.post<AuthRequest, JwtResponse>("/auth/login", AuthRequest(username, password))
                .map(
                    onSuccess = { it.token },
                    onAfterSuccess = { sessionManager.saveToken(it)}
                )
        }
        return result
    }

    suspend fun logout(){
        sessionManager.clearToken()
    }

    suspend fun validToken(): String?{
        val token = sessionManager.getToken() ?: ""
        return if(sessionManager.isTokenValid(token)) token else  null
    }
}