package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository){
    suspend operator fun invoke(username: String, password: String): ApiResult<LoginResponse> {
        return authRepository.login(username, password)
    }
}