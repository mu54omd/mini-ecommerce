package com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.RegisterResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResult<LoginResponse>
    suspend fun register(username: String, password: String, email: String): ApiResult<RegisterResponse>
    suspend fun logout()
    suspend fun validToken(): String?
    suspend fun getUserInfo(): User
    suspend fun clearToken()
}