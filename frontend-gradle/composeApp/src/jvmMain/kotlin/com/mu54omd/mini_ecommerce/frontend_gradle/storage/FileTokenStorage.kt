package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import java.io.File

class FileTokenStorage: TokenStorage {
    private val file = File(System.getProperty("user.home"), ".mini_ecommerce_token")

    override suspend fun saveToken(token: String) {
        file.writeText(token)
    }

    override suspend fun getToken(): String? = if(file.exists()) file.readText().takeIf { it.isNotBlank() } else null

    override suspend fun clearToken() {
        if (file.exists()) file.delete()
    }
}