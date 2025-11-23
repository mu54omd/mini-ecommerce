package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableCard(
    initialOffset: Float = 0f,
    maxSwipe: Float = 80f,
    onCardClick: () -> Unit,
    content: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    val animatableOffset = remember { Animatable(initialOffset) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(animatableOffset.value.roundToInt(), 0) }
                .pointerHoverIcon(icon = PointerIcon.Hand)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            scope.launch {
                                val isOpen = animatableOffset.value != 0f
                                println(isOpen)
                                if (isOpen) {
                                        animatableOffset.animateTo(0f)
                                } else {
                                    onCardClick()
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val target = if (animatableOffset.value > -maxSwipe / 2f) 0f else -maxSwipe
                            scope.launch {
                                animatableOffset.animateTo(
                                    target,
                                    animationSpec = tween(
                                        durationMillis = 250,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        val newOffset = animatableOffset.value + dragAmount
                        scope.launch {
                            animatableOffset.snapTo(newOffset.coerceIn(-maxSwipe, 0f))
                        }
                    }
                }
        ) {
            content()
        }
    }
}