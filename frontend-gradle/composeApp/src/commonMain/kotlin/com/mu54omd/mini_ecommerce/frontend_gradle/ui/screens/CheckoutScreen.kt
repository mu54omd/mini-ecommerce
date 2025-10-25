package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>(),
    onBack: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    val cartState = cartViewModel.cartState.collectAsState().value

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Checkout", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        if (cartState is UiState.Success) {
            cartState.data.items.forEach {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(it.product.name)
                    Text("x${it.quantity}")
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    cartViewModel.checkout()
                    onConfirmClick()
                }) {
                    Text("Confirm Checkout")
                }
                Button(onClick = onBack) { Text("Back") }
            }
        }
    }
}