package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.MainViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.getSessionManager
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.CartScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.CheckoutScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.HomeScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.LoginScreen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.OrdersScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun App(
    authViewModel: AuthViewModel = koinViewModel<AuthViewModel>()
) {

    var screen by rememberSaveable { mutableStateOf("login") }
    MaterialTheme {
        when (screen) {
            "login" -> LoginScreen(onLoginSuccess = { screen = "home" })
            "home" -> HomeScreen(
                onCartClick = {screen = "cart"},
                onOrderClick = { screen = "order" },
                onLogoutClick = {
                    screen = "login"
                    authViewModel.logout()
                }
                )
            "cart" -> CartScreen (
                onBack = { screen = "home" },
                onCheckoutClick = { screen = "checkout"}
            )
            "checkout" -> CheckoutScreen(
                onBack = { screen = "home"},
                onConfirmClick = { screen = "order"}
            )
            "order" -> OrdersScreen(onBack = { screen = "home" })
        }
    }
}