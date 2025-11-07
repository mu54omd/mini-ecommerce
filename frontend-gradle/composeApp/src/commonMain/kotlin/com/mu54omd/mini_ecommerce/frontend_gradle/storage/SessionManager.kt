package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.User

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SessionManager {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
    fun isTokenValid(token: String): Boolean
    fun getUserInfo(token: String): User
}