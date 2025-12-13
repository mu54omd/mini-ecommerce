package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderStatus
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.LoadingView
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components.OrdersFilterChips
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components.UserOrdersList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components.OrdersList

@Composable
fun OrdersScreen(
    isCompact: Boolean = false,
    orderViewModel: OrderViewModel,
    userRole: UserRole,
    onExit: (UiState<*>) -> Unit
) {
    val groupedOrders = orderViewModel.groupedOrders.collectAsState().value
    val updateStatusSummary = orderViewModel.updateStatusSummary.collectAsState().value
    val userOrdersState = orderViewModel.userOrdersState.collectAsState().value
    val lazyListState = rememberLazyListState()
    var expandedGroups by rememberSaveable { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var selectedChip by rememberSaveable { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
        if(userRole == UserRole.USER){
            orderViewModel.getUserOrders()
        }else if(userRole == UserRole.ADMIN) {
            orderViewModel.getGroupedOrders()
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if(userRole == UserRole.ADMIN) {
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
                        OrdersFilterChips(
                            isCompact = isCompact,
                            selectedChip = selectedChip,
                            onChipSelected = { index ->
                                selectedChip = index
                                when(index) {
                                    0 -> {
                                        orderViewModel.setStatusFilter(null)
                                        orderViewModel.getGroupedOrders()
                                    }
                                    1 -> {
                                        orderViewModel.setStatusFilter(OrderStatus.CREATED)
                                        orderViewModel.searchOrders(OrderStatus.CREATED.name)
                                    }
                                    2 -> {
                                        orderViewModel.setStatusFilter(OrderStatus.PAID)
                                        orderViewModel.searchOrders(OrderStatus.PAID.name)
                                    }
                                    3 -> {
                                        orderViewModel.setStatusFilter(OrderStatus.SHIPPED)
                                        orderViewModel.searchOrders(OrderStatus.SHIPPED.name)
                                    }
                                    4 -> {
                                        orderViewModel.setStatusFilter(OrderStatus.CANCELLED)
                                        orderViewModel.searchOrders(OrderStatus.CANCELLED.name)
                                    }
                                }
                            }
                        )
                        OrdersList(
                            lazyListState = lazyListState,
                            groupedOrders = groupedOrders.data,
                            expandedGroups = expandedGroups,
                            onExpandedChange = { expandedGroups = it },
                            onCancelChangesClick = { orderViewModel.getGroupedOrders() },
                            onConfirmChangesClick = { changedOrders ->
                                orderViewModel.updateAllStatusesAndRefresh(changedOrders)
                                selectedChip = 0
                            }
                        )
                        if (updateStatusSummary.isNotEmpty()) {
                            val succeedAttempt =
                                updateStatusSummary.count { it.result is UiState.Success }
                            val failedAttempt =
                                updateStatusSummary.count { it.result is UiState.Error }
                            AlertModal(
                                message = "$succeedAttempt order(s): Updated - $failedAttempt order(s): Failed",
                                onConfirmClick = {
                                    orderViewModel.resetUpdateStatusSummary()
                                }
                            )
                        }
                    }
                }

                else -> onExit(groupedOrders)
            }
        }else if(userRole == UserRole.USER) {
            when (userOrdersState) {
                is UiState.Idle -> {}
                is UiState.Loading -> LoadingView()
                is UiState.Success<List<OrderResponse>> -> {
                    if (userOrdersState.data.isEmpty()) {
                        EmptyPage("Oops!", "There is no order right now!")
                    } else {
                        UserOrdersList(
                            lazyListState = lazyListState,
                            orderItems = userOrdersState.data
                        )
                    }
                }

                else -> onExit(userOrdersState)
            }
        }
    }
}