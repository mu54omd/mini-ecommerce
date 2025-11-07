package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository

class ClearTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.clearToken()
    }
}