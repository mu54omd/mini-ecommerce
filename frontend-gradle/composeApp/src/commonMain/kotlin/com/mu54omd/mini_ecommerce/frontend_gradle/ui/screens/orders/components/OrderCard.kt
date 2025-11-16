package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun OrderCard(
    order: OrderResponse,
    statusList: List<String> = emptyList(),
    status: Int,
    isOrderStatusExpanded: Boolean = false,
    isStatusButtonEnabled: Boolean = false,
    onOrderStatusClick: () -> Unit = {},
    onOrderStatusMenuItemClick: (String) -> Unit = {},
    onOrderStatusMenuDismiss: () -> Unit = {},
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
                            .clickable(enabled = isStatusButtonEnabled) { onOrderStatusClick() }
                            .pointerHoverIcon(if(isStatusButtonEnabled) PointerIcon.Hand else PointerIcon.Default)
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
