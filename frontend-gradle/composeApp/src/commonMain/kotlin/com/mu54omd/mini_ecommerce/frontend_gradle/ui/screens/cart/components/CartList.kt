package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartItemResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.config.GeneratedConfig.BASE_URL
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.CustomAsyncImage

@Composable
fun CartList(
    cartItems: List<CartItemResponse>,
    onClearCartClick: () -> Unit,
    onCheckoutClick: () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        TextButton(
            onClick = onCheckoutClick,
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCartCheckout,
                contentDescription = "Checkout"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Checkout")
        }
        Spacer(Modifier.width(8.dp))
        TextButton(
            onClick = onClearCartClick,
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)

        ) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Cart")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Clear")
        }
    }
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 400.dp, height = 600.dp)
                .verticalScroll(rememberScrollState())
        ) {
            cartItems.forEach { item ->
                var isItemClicked by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .height(if (isItemClicked) 150.dp else 100.dp)
                        .clip(shape = RoundedCornerShape(10))
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright,
                        )
                        .clickable {
                            isItemClicked = !isItemClicked
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomAsyncImage(
                        url = "$BASE_URL${item.product.imageUrl}",
                        contentDescription = item.product.description,
                        errorTint = MaterialTheme.colorScheme.surface,
                        errorImageSize = 150.dp,
                        modifier = Modifier.weight(0.3f)
                    )
                    Column(
                        modifier = Modifier.padding(8.dp).weight(0.7f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.product.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "x${item.quantity}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${item.product.price}$",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (isItemClicked) {
                            HorizontalDivider()
                            Text(
                                item.product.description,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
