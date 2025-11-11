package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

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
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.OrdersList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.LoadingView

@Composable
fun OrdersScreen(
    orderViewModel: OrderViewModel,
    onExit: (UiState<*>) -> Unit
) {
    val ordersState = orderViewModel.ordersState.collectAsState().value
    val orderStatusState = orderViewModel.orderStatusState.collectAsState().value

    LaunchedEffect(Unit) {
        orderViewModel.getAllOrders()
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        when (ordersState) {
            is UiState.Idle -> {}
            is UiState.Loading -> LoadingView()
            is UiState.Success<List<OrderResponse>> -> {
                if (ordersState.data.isEmpty()) {
                    EmptyPage("Oops!", "There is no order right now!")
                } else {
                    OrdersList(
                        orderItems = ordersState.data,
                        onCancelChangesClick = { orderViewModel.getAllOrders() },
                        onConfirmChangesClick = { changedOrders ->
                            changedOrders.forEach { (orderId, newStatus) ->
                                orderViewModel.updateOrderStatus(orderId, newStatus)
                            }
                        }
                    )
                }
            }

            else -> onExit(ordersState)
        }
    }
}