package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import java.io.File

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class SessionManager {
    private val file = File(System.getProperty("user.home"), ".mini_ecommerce_token")

    actual suspend fun saveToken(token: String) {
        file.writeText(token)
    }

    actual suspend fun getToken(): String? = if(file.exists()) file.readText().takeIf { it.isNotBlank() } else null

    actual suspend fun clearToken() {
        if (file.exists()) file.delete()
    }
}