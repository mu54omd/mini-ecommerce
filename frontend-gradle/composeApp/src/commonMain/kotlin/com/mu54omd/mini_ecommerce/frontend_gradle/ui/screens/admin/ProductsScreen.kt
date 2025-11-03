package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AdminViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.admin.components.ProductEditList

@Composable
fun ProductsScreen(
    adminViewModel: AdminViewModel,
    onExit: (UiState<*>) -> Unit,
) {
    val productsState = adminViewModel.productsState.collectAsState().value
    LaunchedEffect(Unit) {
        adminViewModel.refresh()
    }

    when(productsState){
        is UiState.Idle -> {}
        is UiState.Loading -> LoadingView()
        is UiState.Success -> {
            if(productsState.data.isEmpty()){
                EmptyPage("Oops!", "No product found!")
            }else {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    TextButton(onClick = {

                    }){
                        Text("Add Product")
                    }
                    ProductEditList(
                        products = productsState.data,
                        onEditClick = { },
                        onRemoveClick = { productId -> adminViewModel.deleteProduct(productId) },
                        modifier = Modifier.weight(0.9f)
                    )
                }
            }
        }
        else -> onExit(productsState)
    }
}
