package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.OrderList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrdersScreen(
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
                    OrderList(orderItems = ordersState.data)
                }
            }
            else -> onExit(ordersState)
        }
    }
}