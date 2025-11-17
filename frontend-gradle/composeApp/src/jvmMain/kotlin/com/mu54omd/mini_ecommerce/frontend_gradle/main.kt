package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mu54omd.mini_ecommerce.frontend_gradle.di.initKoin

fun main() = application {
    val windowState = rememberWindowState(width = 500.dp, height = 900.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "Mini E-Commerce",
        state = windowState,
    ) {
        initKoin()
        App()
    }
}