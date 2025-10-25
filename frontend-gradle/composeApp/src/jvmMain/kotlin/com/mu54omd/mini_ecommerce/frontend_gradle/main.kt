package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mu54omd.mini_ecommerce.frontend_gradle.di.initKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Mini E-Commerce",

    ) {
        initKoin()
        App()
    }
}