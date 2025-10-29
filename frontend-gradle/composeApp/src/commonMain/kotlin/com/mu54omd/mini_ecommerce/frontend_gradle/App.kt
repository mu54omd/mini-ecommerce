package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.AppNavHost

@Composable
fun App() {
    MaterialTheme {
        AppNavHost()
    }
}