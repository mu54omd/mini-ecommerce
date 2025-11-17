package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OrdersFilterChips(
    selectedChip: Int,
    onChipSelected: (Int) -> Unit
) {
    val labels = listOf("All", "Created", "Paid", "Shipped", "Cancelled")

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, text ->
            FilterChip(
                selected = selectedChip == index,
                onClick = { onChipSelected(index) },
                label = { Text(text) }
            )
        }
    }
}