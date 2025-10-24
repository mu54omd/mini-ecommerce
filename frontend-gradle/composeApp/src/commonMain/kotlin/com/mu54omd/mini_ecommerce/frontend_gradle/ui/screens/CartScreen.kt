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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel = koinViewModel<CartViewModel>(), onBack: () -> Unit) {
    val cartState by cartViewModel.cartState
    val loading by cartViewModel.loading

    LaunchedEffect(Unit) { cartViewModel.refresh() }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Cart", style = MaterialTheme.typography.titleLarge)
            Button(onClick = onBack) { Text("Back") }
        }
        Spacer(Modifier.height(8.dp))
        if (loading) {
            CircularProgressIndicator()
        } else {
            val items = cartState?.items ?: emptyList()
            if (items.isEmpty()) {
                Text("Cart is empty")
            } else {
                items.forEach { item ->
                    Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(item.product.name)
                        Text("x${item.quantity}")
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row {
                    Button(onClick = { cartViewModel.clear() }) { Text("Clear Cart") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { /* checkout */ }) { Text("Checkout") }
                }
            }
        }
    }
}