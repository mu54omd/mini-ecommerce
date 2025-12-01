package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    Products("products", "Products", Icons.Default.Storefront, "Products Screen"),
    Login("login", "Login", Icons.AutoMirrored.Filled.Login, "Login Screen"),
    Cart("cart", "My Cart", Icons.Default.ShoppingCart, "User Cart Screen"),
    Users("users", "Users", Icons.Default.SwitchAccount, "Admin Users Screen"),
    Orders("orders", "Orders", Icons.Default.ShoppingBasket, "Orders Screen"),
    Admin("admin", "Admin", Icons.Default.AdminPanelSettings, "Admin Panel Screen"),
}
