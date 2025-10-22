package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "frontend-gradle",
    ) {
        App()
    }
}