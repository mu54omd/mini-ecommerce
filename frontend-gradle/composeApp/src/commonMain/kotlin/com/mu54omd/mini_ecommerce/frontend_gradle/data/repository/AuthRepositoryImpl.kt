package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.AuthRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.HealthResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.RegisterResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.User
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserRegisterRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.SessionManager

class AuthRepositoryImpl(
    private val api: ApiClient,
    private val sessionManager: SessionManager
): AuthRepository {
    override suspend fun checkHealth(): ApiResult<HealthResponse> {
        return api.get<HealthResponse>("/health")
    }
    override suspend fun login(username: String, password: String): ApiResult<LoginResponse> {
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

    override suspend fun register(username: String, password: String, email: String): ApiResult<RegisterResponse>{
        return api.post<UserRegisterRequest, RegisterResponse>("/auth/register", UserRegisterRequest(username, password, email))
            .map(onSuccess = { it })
    }

    override suspend fun logout(){
        sessionManager.clearToken()
    }

    override suspend fun validToken(): String?{
        val token = sessionManager.getToken() ?: ""
        return if(sessionManager.isTokenValid(token)) token else null
    }

    override suspend fun getUserInfo(): User {
        val token = validToken()
        return token?.let { sessionManager.getUserInfo(it) } ?: User()
    }

    override suspend fun clearToken(){
        sessionManager.clearToken()
    }
}