package com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.NavigationBar(
    isLogin: Boolean,
    isWideScreen: Boolean,
    isNarrowScreen: Boolean,
    navigationDestination: List<Screen>,
    selectedDestination: Int,
    barColor: Color,
    circleColor: Color,
    selectedColor: Color,
    unselectedColor: Color,
    onDestinationClick: (Int, Screen) -> Unit,
) {

    AnimatedContent(
        modifier = Modifier.align(Alignment.BottomCenter).pointerInput(Unit) {},
        targetState = !isLogin && !isWideScreen && !isNarrowScreen,
        transitionSpec = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(durationMillis = 50, delayMillis = 50)
            ) togetherWith slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(durationMillis = 50)
            )
        },
        contentAlignment = Alignment.Center
    ) { state ->
        if (state) {
            AnimatedNavigationBar(
                modifier = Modifier.height(120.dp),
                buttons = navigationDestination,
                enabled = !isLogin,
                selectedItem = selectedDestination,
                onClick = onDestinationClick,
                barColor = barColor,
                circleColor = circleColor,
                selectedColor = selectedColor,
                unselectedColor = unselectedColor
            )
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(0.dp))
        }
    }
    AnimatedContent(
        modifier = Modifier.align(Alignment.CenterStart).pointerInput(Unit) {},
        targetState = !isLogin && isWideScreen && !isNarrowScreen,
        transitionSpec = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(durationMillis = 50, delayMillis = 50)
            ) togetherWith slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(durationMillis = 50)
            )
        },
        contentAlignment = Alignment.Center
    ) { state ->
        if (state) {
            AnimatedNavigationRail(
                modifier = Modifier.width(120.dp),
                enabled = !isLogin,
                buttons = navigationDestination,
                selectedItem = selectedDestination,
                onClick = onDestinationClick,
                barColor = barColor,
                circleColor = circleColor,
                selectedColor = selectedColor,
                unselectedColor = unselectedColor
            )
        } else {
            Box(modifier = Modifier.fillMaxHeight().width(0.dp))
        }
    }
}