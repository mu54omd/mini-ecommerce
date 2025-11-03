package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.user.components.MyOrdersList

@Composable
fun MyOrdersScreen(
    orderViewModel: OrderViewModel,
    onExit: (UiState<*>) -> Unit
) {
    val ordersState = orderViewModel.ordersState.collectAsState().value
    LaunchedEffect(Unit){
        orderViewModel.load()
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        when(ordersState){
            is UiState.Idle -> {}
            is UiState.Loading -> LoadingView()
            is UiState.Success<List<OrderResponse>> -> {
                if(ordersState.data.isEmpty()){
                    EmptyPage("Oops!", "There is no order right now!")
                }else {
                    MyOrdersList(orderItems = ordersState.data)
                }
            }
            else -> onExit(ordersState)
        }
    }
}