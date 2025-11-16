package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartItemResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ProductResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.helper.calculateTotalPrice
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutModal(
    cartItems: List<CartItemResponse>,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onCancelClick,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RectangleShape,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = .5f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceBright,
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
                            modifier = Modifier.fillMaxWidth().padding(2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(it.product.name)
                            Text("X${it.quantity}")
                        }
                        HorizontalDivider()
                    }
                    Spacer(Modifier.height(12.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(start = 24.dp, end = 24.dp), thickness = 3.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Total Price: ${cartItems.calculateTotalPrice()}$", fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    TextButton(
                        onClick = onConfirmClick,
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Confirm Checkout")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Confirm")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onCancelClick()
                            }
                        },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = "Cancel Checkout")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun CheckoutDialogPreview(){
    MaterialTheme {
        CheckoutModal(
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