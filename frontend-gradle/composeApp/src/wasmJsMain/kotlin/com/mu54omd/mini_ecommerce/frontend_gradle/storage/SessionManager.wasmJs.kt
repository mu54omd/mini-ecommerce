package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import kotlinx.browser.localStorage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import org.w3c.dom.get

@OptIn(ExperimentalWasmJsInterop::class)
fun payload(base64: String): String = js("atob(base64)")

@OptIn(ExperimentalWasmJsInterop::class)
fun now(): Double = js("Math.floor(Date.now() / 1000)")
@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class SessionManager {
    actual suspend fun saveToken(token: String) {
        localStorage.setItem("jwt", token);
    }
    actual suspend fun getToken(): String? = localStorage["jwt"]
    actual suspend fun clearToken() {
        localStorage.removeItem("jwt")
    }

    actual fun isTokenValid(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size != 3) return false
        return try {
            val base64 = parts[1]
                .replace('-', '+')
                .replace('_', '/')
                .let { it.padEnd((it.length + 3) / 4 * 4, '=') }
            val payload = payload(base64)
            println("payload: $payload")
            val json = Json.parseToJsonElement(payload).jsonObject
            val exp = json["exp"]?.jsonPrimitive?.longOrNull ?: return false
            println("exp: $exp")
            val now = now()
            println("now: $now")
            exp > now.toLong()
        } catch (e: Exception) {
            println("Error decoding token: ${e.message}")
            false
        }
    }
}