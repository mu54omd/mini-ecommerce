package com.mu54omd.mini_ecommerce.frontend_gradle.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object Orders : Screen("orders")
}
