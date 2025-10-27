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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    val cartState = cartViewModel.cartState.collectAsState().value
    val checkoutState = cartViewModel.checkoutState.collectAsState().value
    var message by remember { mutableStateOf("")}

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Checkout", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Button(onClick = onBack) { Text("Back") }
        }
        Spacer(Modifier.height(8.dp))
        if (cartState is UiState.Success) {
            cartState.data.items.forEach {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(it.product.name)
                    Text("x${it.quantity}")
                }
            }
            Text(text = message)

        }
        Spacer(Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().weight(0.2f)
        ) {
            Button(
                onClick = {
                    cartViewModel.checkout()
                    if(checkoutState is UiState.Success) {
                        onConfirmClick()
                    }else{
                        message = checkoutState.toString()
                    }
                }
            ) { Text("Confirm Checkout") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onCancelClick ) { Text("Cancel") }
        }
    }
}