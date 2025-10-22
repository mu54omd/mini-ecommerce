package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import kotlinx.browser.localStorage
import org.w3c.dom.get

class BrowserTokenStorage: TokenStorage {
    override suspend fun saveToken(token: String) {
        localStorage.setItem("jwt", token);
    }
    override suspend fun getToken(): String? = localStorage["jwt"]
    override suspend fun clearToken() {
        localStorage.removeItem("jwt")
    }
}