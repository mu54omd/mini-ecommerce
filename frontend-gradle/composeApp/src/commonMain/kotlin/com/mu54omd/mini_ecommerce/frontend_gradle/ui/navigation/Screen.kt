package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.ui.graphics.vector.ImageVector
enum class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    Home("home", "Home", Icons.Default.Home, "Home Screen"),
    Login("login", "Login", Icons.AutoMirrored.Filled.Login, "Login Screen"),
    Cart("cart", "My Cart", Icons.Default.ShoppingCart, "User Cart Screen"),
    MyOrders("myOrders", "My Orders", Icons.Default.ShoppingCart, "My Orders Cart Screen"),
    Users("users", "Users", Icons.Default.SwitchAccount, "Admin Users Screen"),
    Orders("orders", "Orders", Icons.Default.ShoppingBasket, "Orders Screen"),
    Products("products", "Products", Icons.Default.Edit, "Products Screen"),
    Admin("admin", "Admin", Icons.Default.AdminPanelSettings, "Admin Panel Screen"),
}
