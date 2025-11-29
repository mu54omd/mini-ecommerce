package com.mu54omd.mini_ecommerce.frontend_gradle.preference

import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class ThemeStorage {
    private val key = "dark_mode"
    private val state = MutableStateFlow(window.localStorage.getItem(key)?.toBoolean() ?: false)

    actual val darkMode: Flow<Boolean> = state

    actual suspend fun setDarkMode(enabled: Boolean) {
        window.localStorage.setItem(key, enabled.toString())
        state.value = enabled
    }
}