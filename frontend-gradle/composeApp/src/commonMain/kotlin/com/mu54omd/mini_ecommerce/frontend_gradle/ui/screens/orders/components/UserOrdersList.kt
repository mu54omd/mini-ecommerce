package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper.getOrderStatusColor

@Composable
fun UserOrdersList(
    lazyListState: LazyListState = rememberLazyListState(),
    statusList: List<String> = listOf("CREATED", "PAID", "SHIPPED", "CANCELLED"),
    orderItems: List<OrderResponse>
) {
    LazyColumn(
        state = lazyListState
    ) {
        items( items =orderItems, key = { orderItem -> orderItem.id }) { order ->
            val status = statusList.indexOf(order.status.uppercase())
            OrderCard(
                order = order,
                statusList = statusList,
                status = status
            )
        }
    }
}