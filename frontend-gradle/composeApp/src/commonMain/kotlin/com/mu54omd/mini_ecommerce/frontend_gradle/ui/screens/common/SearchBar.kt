package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.AppThemeExtras

@Composable
fun SearchBar(
    placeHolderText: String = "Search",
    onQuery: (String) -> Unit,
    onClearQuery: () -> Unit,
) {
    var searchBarText by remember { mutableStateOf("") }

    var isCompact by remember { mutableStateOf(true) }
    val animatedWidth by animateDpAsState(
        targetValue = if (isCompact) 40.dp else 200.dp
    )
    val lineBrush = AppThemeExtras.brushes.lineBrush

    val searchBarFocusRequester = remember { FocusRequester() }

    BasicTextField(
        modifier = Modifier.focusRequester(searchBarFocusRequester),
        cursorBrush = Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.onBackground,
                MaterialTheme.colorScheme.onPrimaryContainer,
                MaterialTheme.colorScheme.onSecondaryContainer)
        ),
        value = searchBarText,
        onValueChange = {
            searchBarText = it
            if (it.isBlank()) {
                onClearQuery()
            } else {
                onQuery(it)
            }
        },
        textStyle = MaterialTheme.typography.bodySmall.copy(brush = lineBrush),
        maxLines = 1,
        decorationBox = { inner ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(animatedWidth)
                    .height(40.dp)
                    .drawBehind{
                        if(!isCompact) {
                            drawRoundRect(
                                brush = lineBrush,
                                style = Stroke(width = 1.dp.toPx()),
                                cornerRadius = CornerRadius(x = 50.dp.toPx())
                            )
                        }
                    }
            ) {
                IconButton(
                    onClick = {
                        isCompact = !isCompact
                        if(!isCompact) {
                            searchBarFocusRequester.requestFocus()
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .pointerHoverIcon(PointerIcon.Hand),
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Bar Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AnimatedContent(
                        targetState = searchBarText.isBlank(),
                        contentAlignment = Alignment.CenterStart,
                        transitionSpec = { expandHorizontally(tween(50)) + fadeIn() togetherWith shrinkHorizontally(tween(50)) + fadeOut() },
                    ) { state ->
                        if (state) {
                            Text(
                                text = placeHolderText,
                                style = MaterialTheme.typography.bodySmall.copy(brush = lineBrush),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                            )
                        }else{
                            Box(modifier = Modifier.fillMaxSize())
                        }
                    }
                    inner()
                }
                AnimatedContent(
                    targetState = searchBarText.isNotBlank(),
                    transitionSpec = {
                        scaleIn(animationSpec = tween(100)) togetherWith scaleOut(
                            animationSpec = tween(100)
                        )
                    },
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.dp)
                ) { state ->
                    if (state) {
                        IconButton(
                            onClick = {
                                searchBarText = ""
                                onClearQuery()
                            },
                            modifier = Modifier
                                .size(25.dp)
                                .pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search Bar Icon",
                            )
                        }
                    }
                }
            }
        }
    )
}