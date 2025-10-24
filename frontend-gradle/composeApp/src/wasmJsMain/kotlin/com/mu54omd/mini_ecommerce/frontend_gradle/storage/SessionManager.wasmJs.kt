package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import kotlinx.browser.localStorage
import org.w3c.dom.get

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class SessionManager {
    actual suspend fun saveToken(token: String) {
        localStorage.setItem("jwt", token);
    }
    actual suspend fun getToken(): String? = localStorage["jwt"]
    actual suspend fun clearToken() {
        localStorage.removeItem("jwt")
    }
}