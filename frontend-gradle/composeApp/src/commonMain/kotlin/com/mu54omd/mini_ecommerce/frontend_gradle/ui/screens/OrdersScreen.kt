package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.OrdersList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.components.LoadingView
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(
    orderViewModel: OrderViewModel,
    onExit: (UiState<*>) -> Unit
) {
    val groupedOrders = orderViewModel.groupedOrders.collectAsState().value
    var expandedGroups by rememberSaveable { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    LaunchedEffect(Unit) {
        orderViewModel.getGroupedOrders()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        when (groupedOrders) {
            is UiState.Idle -> {}
            is UiState.Loading -> LoadingView()
            is UiState.Success<Map<String, List<OrderResponse>>> -> {
                if (groupedOrders.data.isEmpty()) {
                    EmptyPage("Oops!", "There is no order right now!")
                } else {
                    LaunchedEffect(groupedOrders.data.keys) {
                        if (expandedGroups.isEmpty()) {
                            expandedGroups = groupedOrders.data.keys.associateWith { false }
                        }
                    }
                    OrdersList(
                        groupedOrders = groupedOrders.data,
                        expandedGroups = expandedGroups,
                        onExpandedChange = { expandedGroups = it },
                        onCancelChangesClick = { orderViewModel.getGroupedOrders() },
                        onConfirmChangesClick = { changedOrders ->
                            orderViewModel.updateAllStatusesAndRefresh(changedOrders)
                        }
                    )
                }
            }
            else -> onExit(groupedOrders)
        }
    }
}