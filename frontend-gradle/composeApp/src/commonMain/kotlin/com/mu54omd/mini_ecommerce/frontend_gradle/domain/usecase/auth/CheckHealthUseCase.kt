package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.HealthResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository

class CheckHealthUseCase(private val authRepository: AuthRepository){
    suspend operator fun invoke(): ApiResult<HealthResponse> {
        return authRepository.checkHealth()
    }
}