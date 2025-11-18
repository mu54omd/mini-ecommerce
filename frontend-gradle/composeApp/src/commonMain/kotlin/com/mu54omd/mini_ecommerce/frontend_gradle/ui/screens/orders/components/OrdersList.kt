package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper.getOrderStatusColor
import kotlin.collections.mutableMapOf
import kotlin.collections.set

@Composable
fun OrdersList(
    lazyListState: LazyListState = rememberLazyListState(),
    statusList: List<String> = listOf("CREATED", "PAID", "SHIPPED", "CANCELLED"),
    groupedOrders: Map<String, List<OrderResponse>>,
    expandedGroups: Map<String, Boolean>,
    onExpandedChange: (Map<String, Boolean>) -> Unit,
    onCancelChangesClick: () -> Unit,
    onConfirmChangesClick: (Map<Long, String>) -> Unit,
) {
    var isStatusChanged by rememberSaveable { mutableStateOf(false) }
    val changedOrder = rememberSaveable { mutableMapOf<Long, String>() }

    AnimatedVisibility(
        visible = isStatusChanged,
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        enter = slideInVertically(initialOffsetY = {it}, animationSpec = tween(100)),
        exit = slideOutVertically(targetOffsetY = {it}, animationSpec = tween(100))
    ) {
        Row(
            modifier = Modifier
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {
                    isStatusChanged = false
                    onCancelChangesClick()
                    changedOrder.clear()
                },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)

            ) {
                Text(text = "Cancel Changes")
            }
            TextButton(
                onClick = {
                    isStatusChanged = false
                    onConfirmChangesClick(changedOrder)
                },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text(text = "Confirm Changes")
            }
        }
    }
    LazyColumn(
        state = lazyListState
    ) {
        groupedOrders.forEach { (username, orderItems) ->
            val isExpanded = expandedGroups[username] ?: false
            stickyHeader(key = "header_${username}") {
                OrderListHeader(
                    isExpanded = isExpanded,
                    title = username,
                    onItemClick = {
                        onExpandedChange(
                            expandedGroups.toMutableMap().also { it[username] = !isExpanded }
                        )
                    }
                )
            }
            if(isExpanded){
                items(items = orderItems, key = { order -> order.id}) { order ->
                    val status = changedOrder[order.id]
                        ?.let { statusList.indexOf(it) }
                        ?: statusList.indexOf(order.status.uppercase())
                    var isOrderStatusExpanded by remember { mutableStateOf(false) }
                    OrderCard(
                        order = order,
                        statusList = statusList,
                        status = status,
                        isOrderStatusExpanded = isOrderStatusExpanded,
                        isStatusButtonEnabled = true,
                        onOrderStatusClick = { isOrderStatusExpanded = !isOrderStatusExpanded },
                        onOrderStatusMenuItemClick = { filteredStatus ->
                            changedOrder[order.id] = filteredStatus
                            isStatusChanged = true
                            isOrderStatusExpanded = false
                        },
                        onOrderStatusMenuDismiss = { isOrderStatusExpanded = false }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderListHeader(
    isExpanded: Boolean,
    title: String,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onItemClick() }
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            AnimatedContent(targetState = isExpanded){ state ->
                when(state){
                    true -> {
                        Icon(
                            imageVector = Icons.Default.ExpandLess,
                            contentDescription = "Collapse Order List",
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(100)
                            )
                        )
                    }
                    false -> {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Expand Order List",
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(100)
                            )
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiaryContainer,
        )
    }
}
