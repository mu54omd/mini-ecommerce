package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import kotlin.collections.getValue
import kotlin.collections.mutableMapOf

@Composable
fun OrdersList(
    orderItems: List<OrderResponse>,
    onCancelChangesClick: () -> Unit,
    onConfirmChangesClick: (Map<Long, String>) -> Unit,
) {
    val groupedOrders = orderItems.groupBy { it.username }
    val statusList = listOf("CREATED", "PAID", "SHIPPED", "CANCELLED")
    var isStatusChanged by rememberSaveable { mutableStateOf(false) }
    val changedOrder = rememberSaveable { mutableMapOf<Long, String>() }

    AnimatedVisibility(
        visible = isStatusChanged,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(8.dp)
                ) {
                    Text(
                        text = username.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
            items(orderItems) { order ->
                var status by remember { mutableStateOf(statusList.indexOf(order.status.uppercase())) }
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
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
                            Text(
                                text = statusList[status],
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .width(90.dp)
                                    .clip(shape = RoundedCornerShape(40))
                                    .clickable {
                                        status = (status + 1) % 4
                                        isStatusChanged = true
                                        if (changedOrder[order.id].isNullOrBlank()) {
                                            changedOrder[order.id] = statusList[status]
                                        } else {
                                            changedOrder.put(order.id, statusList[status])
                                        }
                                    }
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .background(
                                        color = getOrderStatusColor(statusList[status]),
                                    ).padding(start = 2.dp, end = 2.dp),
                                color = MaterialTheme.colorScheme.surface
                            )
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
        }
    }
}