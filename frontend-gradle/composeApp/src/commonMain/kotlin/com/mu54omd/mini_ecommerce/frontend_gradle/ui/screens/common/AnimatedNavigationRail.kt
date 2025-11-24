package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.navigation.Screen
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.MiniECommerceTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


class RailShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutCenterY = offset
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2

        return Path().apply {
            val cutoutEdgeOffset = cutoutRadius * 1.5f
            val cutoutTopY = cutoutCenterY - cutoutEdgeOffset
            val cutoutBottomY = cutoutCenterY + cutoutEdgeOffset

            // start at top-left (no radius)
            moveTo(0f, 0f)

            // top-right (rounded)
            arcTo(
                rect = Rect(
                    left = size.width - cornerDiameter,
                    top = 0f,
                    right = size.width,
                    bottom = cornerDiameter,
                ),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // right side, going down until cutout
            lineTo(size.width, cutoutTopY)

            // --- CUTOUT ---
            cubicTo(
                x1 = size.width,
                y1 = cutoutCenterY - cutoutRadius,
                x2 = size.width - cutoutRadius,
                y2 = cutoutCenterY - cutoutRadius,
                x3 = size.width - cutoutRadius,
                y3 = cutoutCenterY
            )
            cubicTo(
                x1 = size.width - cutoutRadius,
                y1 = cutoutCenterY + cutoutRadius,
                x2 = size.width,
                y2 = cutoutCenterY + cutoutRadius,
                x3 = size.width,
                y3 = cutoutBottomY
            )
            // --- END CUTOUT ---

            // bottom-right (rounded)
            arcTo(
                rect = Rect(
                    left = size.width - cornerDiameter,
                    top = size.height - cornerDiameter,
                    right = size.width,
                    bottom = size.height,
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // bottom-left (straight)
            lineTo(0f, size.height)

            close()
        }
    }
}


@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    radius: Dp,
    button: Screen,
    iconColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(radius * 2)
            .clip(CircleShape)
            .background(color),
    ) {
        AnimatedContent(
            targetState = button.icon, label = "Bottom bar circle icon",
        ) { targetIcon ->
            Icon(targetIcon, button.contentDescription, tint = iconColor)
        }
    }
}

@Composable
fun AnimatedNavigationRail(
    modifier: Modifier = Modifier,
    buttons: List<Screen>,
    selectedItem: Int,
    onClick: (Int, Screen) -> Unit,
    enabled: Boolean,
    barColor: Color,
    circleColor: Color,
    selectedColor: Color,
    unselectedColor: Color,
) {

    val circleRadius = 26.dp
    var railHeight by remember { mutableStateOf(0) }
    var itemHeight by remember { mutableStateOf(0) }
    var barSize by remember { mutableStateOf(IntSize(0, 0)) }

    val offset = remember(railHeight, itemHeight, selectedItem, buttons) {
        if (railHeight == 0 || itemHeight == 0) 0f
        else {
            val totalItemsHeight = itemHeight * buttons.size
            val topPadding = (railHeight - totalItemsHeight) / 2f

            topPadding + itemHeight * selectedItem + itemHeight / 2f
        }
    }
    val circleRadiusPx = LocalDensity.current.run { circleRadius.toPx().toInt() }
    val offsetTransition = updateTransition(offset, "offset transition")
    val animation = tween<Float>(durationMillis = 100)
    val cutoutOffset by offsetTransition.animateFloat(
        transitionSpec = {
            if (this.initialState == 0f) {
                snap()
            } else {
                animation
            }
        },
        label = "cutout offset"
    ) { it }
    val circleOffset by offsetTransition.animateIntOffset(
        transitionSpec = {
            if (this.initialState == 0f) {
                snap()
            } else {
                tween (animation.durationMillis, animation.delay)
            }
        },
        label = "circle offset"
    ) {
        IntOffset(
            x = circleRadiusPx + 20,
            y = (it - circleRadiusPx).toInt()
        )
    }
    val railShape = remember(cutoutOffset) {
        RailShape(
            offset = cutoutOffset,
            circleRadius = circleRadius,
            cornerRadius = 25.dp,
        )
    }

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
    ) {
        Circle(
            modifier = Modifier
                .offset { circleOffset }
                .zIndex(1f),
            color = circleColor,
            radius = circleRadius,
            button = buttons[selectedItem],
            iconColor = selectedColor,
        )
        Column (
            modifier = Modifier
                .onGloballyPositioned {
                    railHeight = it.size.height
                }
                .onPlaced { barSize = it.size }
                .graphicsLayer {
                    shape = railShape
                    clip = true
                }
                .fillMaxHeight()
                .background(barColor),
            verticalArrangement = Arrangement.Center,
        ) {
            buttons.forEachIndexed { index, button ->
                key(button.route) {
                    val isSelected = index == selectedItem
                    AnimatedNavigationRailItem(
                        modifier = Modifier.onGloballyPositioned {
                            itemHeight = it.size.height
                        },
                        selected = isSelected,
                        enabled = enabled,
                        onClick = { onClick(index, button) },
                        icon = {
                            val iconAlpha by animateFloatAsState(
                                targetValue = if (isSelected) 0f else 1f,
                                label = "Navbar item icon"
                            )
                            Icon(
                                imageVector = button.icon,
                                contentDescription = button.contentDescription,
                                modifier = Modifier.alpha(iconAlpha)
                            )
                        },
                        label = {
                            Text(button.label)
                        },
                        selectedColor = selectedColor,
                        unselectedColor = unselectedColor
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedNavigationRailItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit),
    label: @Composable (() -> Unit),
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.secondary
) {
    val color by animateColorAsState(
        targetValue = if (selected) selectedColor else unselectedColor,
        animationSpec = tween(100)
    )
    val textStyle = MaterialTheme.typography.bodySmall
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(70.dp).then(modifier)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(4.dp)
                .pointerHoverIcon(icon = PointerIcon.Hand)
                .clickable(
                    enabled = enabled,
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    onClick()
                }
        ) {
            CompositionLocalProvider(LocalContentColor provides color, content = icon)
            Spacer(modifier = Modifier.height(8.dp))
            if(!selected) {
                CompositionLocalProvider(
                    LocalContentColor provides color,
                    LocalTextStyle provides textStyle,
                    content = label
                )
            }
        }
    }
}


@Preview
@Composable
fun AnimatedNavigationRailPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        MiniECommerceTheme(darkTheme = false) {
            val buttons = listOf(Screen.Products, Screen.Users, Screen.Orders, Screen.Admin)
            var selectedItem by remember { mutableIntStateOf(0) }
            AnimatedNavigationRail(
                buttons = buttons,
                enabled = true,
                selectedItem = selectedItem,
                onClick = {index, screen -> selectedItem = index},
                barColor = MaterialTheme.colorScheme.primaryContainer,
                circleColor = ExtendedTheme.colorScheme.quaternary.colorContainer,
                selectedColor = ExtendedTheme.colorScheme.quaternary.color,
                unselectedColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
