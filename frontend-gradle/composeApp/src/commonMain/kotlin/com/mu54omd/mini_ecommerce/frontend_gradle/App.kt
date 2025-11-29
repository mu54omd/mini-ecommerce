package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.mu54omd.mini_ecommerce.frontend_gradle.preference.ThemeStorage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.AppNavHost
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App() {
    val themeStorage: ThemeStorage = koinInject()
    val dark by themeStorage.darkMode.collectAsState(false)
    val scope = rememberCoroutineScope()

    MiniECommerceTheme(darkTheme = dark) {
        AppNavHost(
            isDarkTheme = dark,
            onToggleTheme = {
                scope.launch {
                    themeStorage.setDarkMode(!dark)
                }
            }
        )
    }
}