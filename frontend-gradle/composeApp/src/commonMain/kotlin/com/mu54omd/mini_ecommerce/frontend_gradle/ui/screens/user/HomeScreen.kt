package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.user.components.ProductList

@Composable
fun HomeScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onExit: (UiState<*>) -> Unit,
) {
    val productState = productViewModel.products.collectAsState().value
    val cartState = cartViewModel.cartState.collectAsState().value
    val cartItems: Map<Long, Int> = if (cartState is UiState.Success) cartState.data.items.associate { (_, product, quantity) -> product.id to quantity } else emptyMap()
    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
        cartViewModel.refresh()
    }

    when(productState){
        is UiState.Idle -> {}
        is UiState.Loading -> LoadingView()
        is UiState.Success -> {
            if(productState.data.isEmpty()){
                EmptyPage("Oops!", "No product found!")
            }else {
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    ProductList(
                        products = productState.data,
                        cartItems = cartItems,
                        onAddClick = { cartViewModel.add(it) },
                        onRemoveClick = { cartViewModel.remove(it) },
                        modifier = Modifier.weight(0.9f)
                    )
                }
            }
        }
        else -> onExit(productState)
    }
}
