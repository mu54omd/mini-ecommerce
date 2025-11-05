package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getOrderStatusColor(status: String): Color {
    return when (status) {
        "PAID" -> MaterialTheme.colorScheme.secondary
        "SHIPPED" -> MaterialTheme.colorScheme.tertiary
        "CANCELLED" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.inverseSurface
    }
}