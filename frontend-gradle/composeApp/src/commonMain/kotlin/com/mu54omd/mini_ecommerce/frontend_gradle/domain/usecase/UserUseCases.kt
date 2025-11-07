package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user.DeleteUserUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user.EditUserUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user.GetAllUsersUseCase

data class UserUseCases(
    val deleteUserUseCase: DeleteUserUseCase,
    val editUserUseCase: EditUserUseCase,
    val getAllUsersUseCase: GetAllUsersUseCase
)
