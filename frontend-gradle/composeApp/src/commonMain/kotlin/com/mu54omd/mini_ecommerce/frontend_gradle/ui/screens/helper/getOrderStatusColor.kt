package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getOrderStatusColor(status: String): Color {
    return when (status) {
        "PAID" -> MaterialTheme.colorScheme.secondaryContainer
        "SHIPPED" -> MaterialTheme.colorScheme.tertiaryContainer
        "CANCELLED" -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceContainer
    }
}