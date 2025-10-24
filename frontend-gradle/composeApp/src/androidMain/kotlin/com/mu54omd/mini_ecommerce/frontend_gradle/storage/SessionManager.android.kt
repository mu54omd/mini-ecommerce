package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import android.content.Context
import androidx.core.content.edit

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class SessionManager (private val context: Context) {
    private val prefs = context.getSharedPreferences("mini_ecomm", Context.MODE_PRIVATE)
    actual suspend fun saveToken(token: String) {
        prefs.edit { putString("jwt", token) }
    }
    actual suspend fun getToken(): String? = prefs.getString("jwt", null)
    actual suspend fun clearToken() = prefs.edit { remove("jwt") }
}