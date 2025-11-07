package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.User
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository

class GetUserInfoUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): User {
        return authRepository.getUserInfo()
    }
}