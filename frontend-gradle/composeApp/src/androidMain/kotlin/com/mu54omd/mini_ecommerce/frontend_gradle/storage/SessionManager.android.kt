package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class SessionManager (private val context: Context) {
    private val prefs = context.getSharedPreferences("mini_ecomm", Context.MODE_PRIVATE)
    actual suspend fun saveToken(token: String) {
        prefs.edit { putString("jwt", token) }
    }
    actual suspend fun getToken(): String? = prefs.getString("jwt", null)
    actual suspend fun clearToken() = prefs.edit { remove("jwt") }
    actual fun isTokenValid(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size != 3) return false
        return try {
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = Json.parseToJsonElement(payload).jsonObject
            val exp = json["exp"]?.jsonPrimitive?.longOrNull ?: return false
            val now = System.currentTimeMillis() / 1000
            exp > now
        } catch (e: Exception) {
            println("Error decoding token: ${e.message}")
            false
        }
    }

    actual fun getUserInfo(token: String): User {
        val parts = token.split(".")
        if (parts.size != 3) return User()
        return try {
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = Json.parseToJsonElement(payload).jsonObject
            val username = json["sub"]?.jsonPrimitive?.content ?: "guest"
            var role = json["role"]?.jsonPrimitive?.content
            role = when(role){
                "[ROLE_USER]" -> "USER"
                "[ROLE_ADMIN]" -> "ADMIN"
                else -> "GUEST"
            }
            User(username = username, role = role)
        } catch (e: Exception) {
            println("Error parsing token: ${e.message}")
            User()
        }
    }
}