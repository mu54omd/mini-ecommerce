package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.runtime.Composable
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.AppNavHost

@Composable
fun App() {
    MiniECommerceTheme(darkTheme = true) {
        AppNavHost()
    }
}