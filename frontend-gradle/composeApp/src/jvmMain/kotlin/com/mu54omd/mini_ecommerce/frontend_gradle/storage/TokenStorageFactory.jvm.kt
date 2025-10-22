package com.mu54omd.mini_ecommerce.frontend_gradle.storage

actual fun getTokenStorage(): TokenStorage = FileTokenStorage()