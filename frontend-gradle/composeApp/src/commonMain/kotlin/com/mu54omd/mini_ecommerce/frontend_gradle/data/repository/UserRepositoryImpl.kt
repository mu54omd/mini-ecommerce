package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.UserRepository

class UserRepositoryImpl(private val api: ApiClient): UserRepository {

    override suspend fun getAllUsers(): ApiResult<List<UserResponse>> = api.get("/users")
    override suspend fun createUser(user: UserEditRequest): ApiResult<UserResponse> = api.post("/users",user)

    override suspend fun deleteUser(userId: Long): ApiResult<Unit> = api.delete("/users/${userId}")
    override suspend fun editUser(userId: Long, user: UserEditRequest): ApiResult<UserResponse> = api.put("/users/${userId}", user)
}