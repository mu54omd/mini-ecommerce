package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper.getOrderStatusColor

@Composable
fun OrdersFilterChips(
    selectedChip: Int,
    onChipSelected: (Int) -> Unit
) {
    val labels = listOf("ALL", "CREATED", "PAID", "SHIPPED", "CANCELLED")

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, text ->
            FilterChip(
                selected = selectedChip == index,
                onClick = { onChipSelected(index) },
                label = { Text(text) },
                modifier = Modifier.padding(start = 1.dp, end = 1.dp),
                colors = FilterChipDefaults.elevatedFilterChipColors(selectedContainerColor = getOrderStatusColor(text))
            )
        }
    }
}