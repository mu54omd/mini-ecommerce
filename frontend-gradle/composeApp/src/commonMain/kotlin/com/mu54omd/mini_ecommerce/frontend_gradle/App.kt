package com.mu54omd.mini_ecommerce.frontend_gradle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.LoginScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    var token by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        if (token == null) {
            LoginScreen(onLoginSuccess = { jwt -> token = jwt })
        } else {
            Text("✅ Logged in! Token: $token")
        }
    }
}