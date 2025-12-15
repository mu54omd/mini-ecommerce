package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.AppThemeExtras
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.app_icon_dark
import frontend_gradle.composeapp.generated.resources.app_icon_light
import frontend_gradle.composeapp.generated.resources.drawer_divider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppDrawer(
    isDrawerVisible: Boolean,
    isDarkTheme: Boolean,
    username: String = "",
    email: String = "",
    onDismiss: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isDrawerVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) { detectTapGestures { onDismiss() } }
            )
        }
        AnimatedVisibility(
            visible = isDrawerVisible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(400, 50)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(400, 50)
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(350.dp)
                    .background(
                        brush = AppThemeExtras.brushes.drawerBrush,
                        RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                    )
            ) {
                Box(
                ) {
                    Box(
                        modifier = Modifier.height(250.dp)
                    ) {
                        Image(
                            painter = painterResource(if (isDarkTheme) Res.drawable.app_icon_dark else Res.drawable.app_icon_light),
                            contentDescription = "Drawer App Logo",
                            alpha = 0.2f,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                        Image(
                            painter = painterResource(Res.drawable.drawer_divider),
                            contentDescription = "Drawer divider Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(100.dp)
                                .align(Alignment.BottomCenter),
                            alpha = 0.2f
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(start = 24.dp)
                            .offset(y = (-24).dp)
                    ) {
                        Text(
                            text = "Hello $username",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                DrawerItem(title = "Settings", icon = Icons.Default.Settings) {}
                DrawerItem(title = "Help", icon = Icons.AutoMirrored.Filled.Help) {}
                DrawerItem(title = "About", icon = Icons.Default.Info) {}
            }
        }

    }
}

@Preview
@Composable
fun AppDrawerPreview() {
    MiniECommerceTheme(darkTheme = false) {
        AppDrawer(
            isDrawerVisible = true,
            isDarkTheme = isSystemInDarkTheme(),
            username = "User",
            email = "user@gmail.com",
            onDismiss = {})
    }
}