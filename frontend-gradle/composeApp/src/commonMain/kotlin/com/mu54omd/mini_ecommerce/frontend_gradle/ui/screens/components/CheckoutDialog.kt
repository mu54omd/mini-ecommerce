package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartItemResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ProductResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CheckoutDialog(
    cartItems: List<CartItemResponse>,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancelClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shape = RoundedCornerShape(5)
                ).width(300.dp).height(400.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(state = rememberScrollState())
                        .weight(1f)
                ) {
                    cartItems.forEach {
                        Row(
                            modifier = Modifier.padding(2.dp).background(color = MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(10)).fillMaxWidth().padding(2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(it.product.name)
                            Text("X${it.quantity}")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(start = 24.dp, end = 24.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total: ${calculateTotalPrice(cartItems)}")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Button(onClick = onConfirmClick) { Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Confirm Checkout") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = onCancelClick) { Icon(imageVector = Icons.Default.Cancel, contentDescription = "Cancel Checkout") }
                }
            }
        }
    }
}

private fun calculateTotalPrice(cartItems: List<CartItemResponse>): Double{
    return cartItems.fold(0.0) { acc, item -> acc + item.quantity * item.product.price }
}

@Composable
@Preview
fun CheckoutDialogPreview(){
    MaterialTheme {
        CheckoutDialog(
            cartItems = listOf(
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
                CartItemResponse(id = 2, product = ProductResponse(10, "Phone", "Phone", 3000.0), quantity = 4),
            ),
            onCancelClick = {},
            onConfirmClick = {}
        )
    }
}