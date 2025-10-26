package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.mu54omd.mini_ecommerce.frontend_gradle.di.initKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Mini E-Commerce",
        state = WindowState(
            width = 500.dp,
            height = 900.dp
        ),
    ) {
        initKoin()
        App()
    }
}