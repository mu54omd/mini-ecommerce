package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mu54omd.mini_ecommerce.frontend_gradle.di.initKoin
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.icon_32x32
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    val windowState = rememberWindowState(width = 500.dp, height = 900.dp)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Mini E-Commerce",
        icon = painterResource(Res.drawable.icon_32x32),
        state = windowState,
    ) {
        initKoin()
        App()
    }
}