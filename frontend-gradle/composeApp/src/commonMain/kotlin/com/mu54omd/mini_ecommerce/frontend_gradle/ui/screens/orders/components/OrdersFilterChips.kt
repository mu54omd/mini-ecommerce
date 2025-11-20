package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChairAlt
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper.getOrderStatusColor

@Composable
fun OrdersFilterChips(
    isCompact: Boolean = false,
    selectedChip: Int,
    onChipSelected: (Int) -> Unit
) {
    val labels = listOf(
        FilterChipItem( "ALL",Icons.Default.SelectAll, "Show All Orders"),
        FilterChipItem( "CREATED",Icons.Default.ShoppingBag,"Show Created Orders") ,
        FilterChipItem("PAID", Icons.Default.Paid,"Show Paid Orders"),
        FilterChipItem("SHIPPED",Icons.Default.LocalShipping,"Show Shipped Orders"),
        FilterChipItem("CANCELLED",Icons.Default.Cancel,"Show Cancelled Orders")
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, (label, image ,description) ->
            val color = getOrderStatusColor(label)
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
                                color = if(selectedChip == index) color else Color.Transparent
                            )
                        }
                    }
                    .clickable(enabled = selectedChip != index){ onChipSelected(index) }
                    .pointerHoverIcon(icon = PointerIcon.Hand)
                    .padding(1.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(2.dp).widthIn(min = 48.dp)
                ) {
                    Icon(imageVector = image, contentDescription = description)
                    AnimatedContent(targetState = isCompact){ state ->
                        if(!state){
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                }
            }
        }
    }
}
private data class FilterChipItem(
    val label: String,
    val image: ImageVector,
    val description: String,
)