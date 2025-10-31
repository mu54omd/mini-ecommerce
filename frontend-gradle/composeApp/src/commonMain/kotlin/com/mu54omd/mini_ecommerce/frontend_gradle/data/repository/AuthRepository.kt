package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.AuthRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.RegisterResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.User
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserRegisterRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.SessionManager

class AuthRepository(
    private val api: ApiClient,
    private val sessionManager: SessionManager
) {
    suspend fun login(username: String, password: String): ApiResult<LoginResponse> {
        var result = api.post<AuthRequest, LoginResponse>("/auth/login", AuthRequest(username, password))
            .map(
                onSuccess = { it },
                onAfterSuccess = { sessionManager.saveToken(it.response) }
            )
        if(result !is ApiResult.Success){
            sessionManager.clearToken()
            result = api.post<AuthRequest, LoginResponse>("/auth/login", AuthRequest(username, password))
                .map(
                    onSuccess = { it },
                    onAfterSuccess = { sessionManager.saveToken(it.response)}
                )
        }
        return result
    }

    suspend fun register(username: String, password: String, email: String): ApiResult<RegisterResponse>{
        return api.post<UserRegisterRequest, RegisterResponse>("/auth/register", UserRegisterRequest(username, password, email))
            .map(onSuccess = { it })
    }

    suspend fun logout(){
        sessionManager.clearToken()
    }

    suspend fun validToken(): String?{
        val token = sessionManager.getToken() ?: ""
        return if(sessionManager.isTokenValid(token)) token else null
    }

    suspend fun getUserInfo(): User {
        val token = validToken()
        return token?.let { sessionManager.getUserInfo(it) } ?: User()
    }

    suspend fun clearToken(){
        sessionManager.clearToken()
    }
}