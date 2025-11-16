package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderItemResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper.getOrderStatusColor
import kotlin.collections.get
import kotlin.collections.getValue
import kotlin.collections.mutableMapOf
import kotlin.collections.set

@Composable
fun OrdersList(
    groupedOrders: Map<String, List<OrderResponse>>,
    expandedGroups: Map<String, Boolean>,
    onExpandedChange: (Map<String, Boolean>) -> Unit,
    onCancelChangesClick: () -> Unit,
    onConfirmChangesClick: (Map<Long, String>) -> Unit,
) {
    val statusList = listOf("CREATED", "PAID", "SHIPPED", "CANCELLED")
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
    LazyColumn {
        groupedOrders.forEach { (username, orderItems) ->
            val isExpanded = expandedGroups[username] ?: false
            stickyHeader(key = "header_${username}") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                        .clip(shape = RoundedCornerShape(10))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                onExpandedChange(
                                    expandedGroups.toMutableMap().also { it[username] = !isExpanded }
                                )
                            }
                        )
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = username.uppercase(),
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
                }
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
                        onOrderStatusClick = { isOrderStatusExpanded = !isOrderStatusExpanded },
                        onOrderStatusMenuItemClick = { filteredStatus ->
                            changedOrder[order.id] = filteredStatus
                            isStatusChanged = true
                            isOrderStatusExpanded = false
                            println(changedOrder)
                        },
                        onOrderStatusMenuDismiss = { isOrderStatusExpanded = false }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: OrderResponse,
    statusList: List<String>,
    status: Int,
    isOrderStatusExpanded: Boolean,
    onOrderStatusClick: () -> Unit,
    onOrderStatusMenuItemClick: (String) -> Unit,
    onOrderStatusMenuDismiss: () -> Unit,
){
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).animateContentSize(),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                Text(
                    text = "Order #${order.id}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(color = Color.Transparent)
                ) {
                    Text(
                        text = statusList[status],
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .width(90.dp)
                            .clip(shape = RoundedCornerShape(40))
                            .clickable { onOrderStatusClick() }
                            .pointerHoverIcon(PointerIcon.Hand)
                            .background(
                                color = getOrderStatusColor(statusList[status]),
                            ).padding(start = 2.dp, end = 2.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    DropdownMenu(
                        expanded = isOrderStatusExpanded,
                        onDismissRequest = { onOrderStatusMenuDismiss() },
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        modifier = Modifier.width(90.dp)
                    ) {
                        statusList
                            .filter { statusItem -> statusList[status] != statusItem }
                            .forEach { filteredStatus ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = filteredStatus,
                                            style = MaterialTheme.typography.bodyMedium,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .width(90.dp)
                                                .clip(shape = RoundedCornerShape(40))
                                                .pointerHoverIcon(PointerIcon.Hand)
                                                .background(
                                                    color = getOrderStatusColor(
                                                        filteredStatus
                                                    ),
                                                ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = { onOrderStatusMenuItemClick(filteredStatus) },
                                    contentPadding = PaddingValues(2.dp),
                                    modifier = Modifier.height(24.dp)
                                        .clip(shape = RoundedCornerShape(40))
                                        .background(color = Color.Transparent),
                                )
                            }
                    }
                }
            }
            HorizontalDivider(
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.surface
            )
            Column(modifier = Modifier.padding(12.dp)) {
                order.items.forEach {
                    Text(
                        text = "- ${it.productName} x${it.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                    )
                }
                Text(
                    text = "${order.totalPrice}$",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}
