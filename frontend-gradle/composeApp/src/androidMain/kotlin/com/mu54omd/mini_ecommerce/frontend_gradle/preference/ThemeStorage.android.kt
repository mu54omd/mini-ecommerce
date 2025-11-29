package com.mu54omd.mini_ecommerce.frontend_gradle.preference

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.map

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ThemeStorage(private val context: Context) {
    private val dataStore = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("settings.preferences_pb")
    }

    private object Keys {
        val DARK = booleanPreferencesKey("dark_mode")
    }

    actual val darkMode = dataStore.data.map { it[Keys.DARK] ?: false }

    actual suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[Keys.DARK] = enabled }
    }
}