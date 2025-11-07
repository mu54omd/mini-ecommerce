package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.ClearTokenUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.GetUserInfoUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.LoginUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.LogoutUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.RegisterUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.ValidateTokenUseCase

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val logoutUseCase: LogoutUseCase,
    val clearTokenUseCase: ClearTokenUseCase,
    val getUserInfoUseCase: GetUserInfoUseCase,
    val validateTokenUseCase: ValidateTokenUseCase
)