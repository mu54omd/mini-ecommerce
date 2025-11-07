package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission.PermissionChecker
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.UserRepository

class DeleteUserUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val permissionChecker: PermissionChecker
) {
    suspend operator fun invoke(userId: Long): ApiResult<Unit>{
        val user = authRepository.getUserInfo()
        if(!permissionChecker.canManageUser(user)) return ApiResult.Unauthorized()
        return userRepository.deleteUser(userId = userId)
    }
}