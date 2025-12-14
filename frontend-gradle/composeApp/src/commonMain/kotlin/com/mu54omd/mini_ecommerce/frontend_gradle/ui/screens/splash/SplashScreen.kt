package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.app_icon_dark
import frontend_gradle.composeapp.generated.resources.app_icon_light
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen(
    isDarkTheme: Boolean,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)
    ){
        Image(
            painter = painterResource(if(isDarkTheme) Res.drawable.app_icon_dark else Res.drawable.app_icon_light),
            contentDescription = "Splash Screen App Logo",
            modifier = Modifier.size(300.dp)
        )
    }
}