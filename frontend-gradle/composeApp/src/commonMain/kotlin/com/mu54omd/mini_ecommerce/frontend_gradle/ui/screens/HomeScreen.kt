package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.LoadingView
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit,
    onOrderClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onExit: (UiState<*>) -> Unit,
) {
    val productState = productViewModel.products.collectAsState().value
    LaunchedEffect(Unit) { productViewModel.loadProducts() }

    when(productState){
        is UiState.Idle -> {}
        is UiState.Loading -> LoadingView()
        is UiState.Success -> {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Text("Products", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.weight(0.8f)
                ) {
                    items(productState.data) { product ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            onClick = { cartViewModel.add(product.id) },
                            enabled = product.stock > 0
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(product.name, fontWeight = FontWeight.Bold)
                                Text("${product.price} $")
                                Text("#${product.stock}")
                            }
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth().weight(0.2f)
                ) {
                    Button(onClick = onCartClick) { Text("Cart") }
                    Button(onClick = onOrderClick) { Text("Order") }
                    Button(
                        onClick = onLogoutClick
                    ) { Text("Logout") }
                }
            }
        }
        else -> onExit(productState)
    }
}
