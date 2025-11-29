package com.mu54omd.mini_ecommerce.frontend_gradle.preference

import kotlinx.coroutines.flow.Flow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ThemeStorage {
    val darkMode: Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
}