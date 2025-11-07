package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.RegisterResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String, email: String): ApiResult<RegisterResponse> {
        return authRepository.register(username, password, email)
    }
}