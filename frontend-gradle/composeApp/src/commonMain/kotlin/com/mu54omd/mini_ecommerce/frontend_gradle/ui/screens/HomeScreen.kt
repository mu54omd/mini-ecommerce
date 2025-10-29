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
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.ProductList
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.mapOf

@Composable
fun HomeScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onExit: (UiState<*>) -> Unit,
) {
    val productState = productViewModel.products.collectAsState().value
    val cartState = cartViewModel.cartState.collectAsState().value
    val cartItems: Map<Long, Int> = if (cartState is UiState.Success) cartState.data.items.associate { (_, product, quantity) -> product.id to quantity } else emptyMap()
    LaunchedEffect(Unit) { productViewModel.loadProducts() }

    when(productState){
        is UiState.Idle -> {}
        is UiState.Loading -> LoadingView()
        is UiState.Success -> {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                ProductList(
                    products = productState.data,
                    cartItems = cartItems,
                    onAddClick = { cartViewModel.add(it)},
                    onRemoveClick = { cartViewModel.remove(it)},
                    modifier = Modifier.weight(0.9f)
                )
            }
        }
        else -> onExit(productState)
    }
}
