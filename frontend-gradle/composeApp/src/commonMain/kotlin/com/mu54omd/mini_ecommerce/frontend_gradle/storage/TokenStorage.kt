package com.mu54omd.mini_ecommerce.frontend_gradle.storage

interface TokenStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}