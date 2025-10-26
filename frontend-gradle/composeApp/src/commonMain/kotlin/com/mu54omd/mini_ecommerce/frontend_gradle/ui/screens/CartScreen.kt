package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EmptyPage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onExit: (UiState<*>) -> Unit,
    onCheckoutClick: () -> Unit,
) {
    val cartState = cartViewModel.cartState.collectAsState().value

    LaunchedEffect(Unit) { cartViewModel.refresh() }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Cart", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Button(onClick = onBack) { Text("Back") }
        }
        Spacer(Modifier.height(8.dp))
        when(cartState){
            is UiState.Idle -> {}
            is UiState.Loading -> { CircularProgressIndicator() }
            is UiState.Success<CartResponse> -> {
                val items = cartState.data.items
                if (items.isEmpty()) {
                    EmptyPage(title = "Oops!", message = "Your cart is empty")
                } else {
                    Column(
                        modifier = Modifier.weight(0.8f)
                    ) {
                        items.forEach { item ->
                            Row(
                                Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.product.name)
                                Text("x${item.quantity}")
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().weight(0.2f)
                    ) {
                        Button(onClick = { cartViewModel.clear() }) { Text("Clear Cart") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = onCheckoutClick ) { Text("Checkout") }
                    }
                }
            }
            else -> onExit(cartState)
        }
    }
}