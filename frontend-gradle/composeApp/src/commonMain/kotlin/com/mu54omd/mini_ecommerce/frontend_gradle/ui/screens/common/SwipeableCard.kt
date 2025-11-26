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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableCard(
    initialOffset: Dp = 0.dp,
    maxSwipe: Dp = 80.dp,
    onCardClick: () -> Unit,
    content: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    val density = LocalDensity.current
    val animOffsetDp = remember { Animatable(initialOffset.value) }
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
                .offset {
                    val px = with(density) { animOffsetDp.value.dp.roundToPx() }
                    IntOffset(px, 0)
                }
                .pointerHoverIcon(icon = PointerIcon.Hand)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            scope.launch {
                                val isOpen = animOffsetDp.value != 0f
                                println(isOpen)
                                if (isOpen) {
                                        animOffsetDp.animateTo(0f)
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
                            val half = -maxSwipe.value / 2f
                            val target = if (animOffsetDp.value > half) 0f else -maxSwipe.value
                            scope.launch {
                                animOffsetDp.animateTo(
                                    target,
                                    tween(250, easing = FastOutSlowInEasing)
                                )
                            }
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            val new = animOffsetDp.value + with(density) { dragAmount.toDp().value }
                            animOffsetDp.snapTo(new.coerceIn(-maxSwipe.value, 0f))
                        }
                    }
                }
        ) {
            content()
        }
    }
}