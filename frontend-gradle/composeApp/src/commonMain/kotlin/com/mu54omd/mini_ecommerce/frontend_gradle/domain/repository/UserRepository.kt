package com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse

interface UserRepository {
    suspend fun getAllUsers(): ApiResult<List<UserResponse>>
    suspend fun createUser(user: UserEditRequest): ApiResult<UserResponse>
    suspend fun deleteUser(userId: Long): ApiResult<Unit>
    suspend fun editUser(userId: Long, user: UserEditRequest): ApiResult<UserResponse>
}