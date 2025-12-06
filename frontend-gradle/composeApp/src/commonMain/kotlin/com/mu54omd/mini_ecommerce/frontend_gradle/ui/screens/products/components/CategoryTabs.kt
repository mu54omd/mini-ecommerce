package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.products.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.theme.ExtendedTheme
import kotlinx.coroutines.launch

@Composable
fun CategoryTabs(
    categoriesState: State<UiState<List<String>>>,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    onExit: (UiState<*>) -> Unit
) {
    when(categoriesState.value) {
        is UiState.Idle -> {}
        is UiState.Loading -> {}
        is UiState.Success -> {
            val state = rememberLazyListState()
            val scope = rememberCoroutineScope()
            val categories = (categoriesState.value as UiState.Success<List<String>>).data
            val list = (listOf("All") + categories)
            val color = ExtendedTheme.colorScheme.quaternary.colorContainer

            LazyRow(
                modifier = Modifier.fillMaxWidth().draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val first = state.firstVisibleItemIndex
                            val offset = state.firstVisibleItemScrollOffset - delta.toInt()
                            state.scrollToItem(index = first, scrollOffset = offset)
                        }
                    }
                ),
                state = state,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(8.dp)
            ) {
                items(items = list, key = { item -> item}) { item ->

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(3.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(40)
                            )
                            .clip(shape = RoundedCornerShape(40))
                            .drawWithCache{
                                onDrawBehind {
                                    drawRect(
                                        color = if((if(item == "All") null else item) == selectedCategory) color else Color.Transparent
                                    )
                                }
                            }
                            .clickable(enabled = selectedCategory != item){  onSelectCategory(if(item == "All") null else item) }
                            .pointerHoverIcon(icon = PointerIcon.Hand)
                            .padding(1.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(4.dp).widthIn(min = 48.dp)
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
        else -> { onExit(categoriesState.value) }
    }
}