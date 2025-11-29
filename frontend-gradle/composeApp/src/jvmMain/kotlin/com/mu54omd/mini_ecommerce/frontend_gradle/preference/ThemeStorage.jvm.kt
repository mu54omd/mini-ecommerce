package com.mu54omd.mini_ecommerce.frontend_gradle.preference

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ThemeStorage {
    private val dataStore = PreferenceDataStoreFactory.createWithPath {
        val home = System.getProperty("user.home")
        val file = "$home/settings.preferences_pb"
        file.toPath()
    }

    private object Keys {
        val DARK = booleanPreferencesKey("dark_mode")
    }

    actual val darkMode = dataStore.data.map { it[Keys.DARK] ?: false }

    actual suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[Keys.DARK] = enabled }
    }
}