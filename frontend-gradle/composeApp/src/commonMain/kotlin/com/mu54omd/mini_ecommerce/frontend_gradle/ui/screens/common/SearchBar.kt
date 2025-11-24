package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme

@Composable
fun SearchBar(
    placeHolderText: String = "Search",
    onQuery: (String) -> Unit,
    onClearQuery: () -> Unit,
) {
    var searchBarText by remember { mutableStateOf("") }
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val errorColor = MaterialTheme.colorScheme.error
    val quaternaryColor = ExtendedTheme.colorScheme.quaternary.color
    val quinaryColor = ExtendedTheme.colorScheme.quinary.color
    var isCompact by remember { mutableStateOf(true) }
    val animatedWidth by animateDpAsState(
        targetValue = if (isCompact) 40.dp else 200.dp
    )
    val lineBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                primaryColor,
                secondaryColor,
                tertiaryColor,
                errorColor,
                quinaryColor,
                quaternaryColor
            ).shuffled()
        )
    }

    val searchBarFocusRequester = remember { FocusRequester() }

    BasicTextField(
        modifier = Modifier.focusRequester(searchBarFocusRequester),
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
                    modifier = Modifier.weight(1f).padding(4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AnimatedContent(
                        targetState = searchBarText.isBlank(),
                        transitionSpec = { fadeIn(tween(50)) togetherWith fadeOut(tween(50)) },
                    ) { state ->
                        if (state) {
                            Text(
                                text = placeHolderText,
                                style = MaterialTheme.typography.bodySmall.copy(brush = lineBrush),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
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