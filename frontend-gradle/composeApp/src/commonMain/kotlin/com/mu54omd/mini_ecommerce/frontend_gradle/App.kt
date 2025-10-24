package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.getSessionManager
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.HomeScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.LoginScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun App() {
    MaterialTheme {
        var screen by remember { mutableStateOf("login") }
        when (screen) {
            "login" -> LoginScreen(onLoginSuccess = { screen = "home" })
            "home" -> HomeScreen(
                onCartClick = {screen = "cart"},
                onLogoutClick = {
                        GlobalScope.launch {
                            getSessionManager().clearToken()
                            screen = "login"
                        }
                    }
                )
            "cart" -> CartScreen() { screen = "home" }
        }
    }
}