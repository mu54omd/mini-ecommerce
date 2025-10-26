package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.io.File
import java.util.Base64

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

    actual fun isTokenValid(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size != 3) return false
        return try {
            val decoder = Base64.getUrlDecoder()
            val payload = String(decoder.decode(parts[1]))
            val json = Json.parseToJsonElement(payload).jsonObject
            val exp = json["exp"]?.jsonPrimitive?.longOrNull ?: return false
            val now = System.currentTimeMillis() / 1000
            exp > now
        } catch (e: Exception) {
            println("Error parsing token: ${e.message}")
            false
        }
    }
}