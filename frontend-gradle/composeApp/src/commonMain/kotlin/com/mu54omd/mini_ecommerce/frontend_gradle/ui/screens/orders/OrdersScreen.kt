package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderStatus
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderUiEffect
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.AlertModal
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.common.EmptyPage
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components.OrdersFilterChips
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components.OrdersList
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.orders.components.UserOrdersList

@Composable
fun OrdersScreen(
    isCompact: Boolean = false,
    orderViewModel: OrderViewModel,
    userRole: UserRole,
    onExit: (UiState<*>) -> Unit
) {
    val state by orderViewModel.state.collectAsState()
    val effect = orderViewModel.effect
    val lazyListState = rememberLazyListState()
    var expandedGroups by rememberSaveable { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var selectedChip by rememberSaveable { mutableIntStateOf(0) }
    var alertMessage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        if (userRole == UserRole.USER) {
            orderViewModel.getUserOrders()
        } else if (userRole == UserRole.ADMIN) {
            orderViewModel.getGroupedOrders()
        }
    }

    LaunchedEffect(Unit) {
        effect.collect { effect ->
            alertMessage = when (effect) {
                is OrderUiEffect.ShowMessage -> {
                    effect.message
                }

                is OrderUiEffect.ShowError -> {
                    effect.message
                }
            }
        }
    }

    Box(Modifier.fillMaxSize().padding(16.dp)) {
        Column {
            if (userRole == UserRole.ADMIN) {
                OrdersFilterChips(
                    isCompact = isCompact,
                    selectedChip = selectedChip,
                    onChipSelected = { index ->
                        selectedChip = index
                        when (index) {
                            0 -> {
                                orderViewModel.onStatusFilterChanged(null)
                            }

                            1 -> {
                                orderViewModel.onStatusFilterChanged(OrderStatus.CREATED)
                            }

                            2 -> {
                                orderViewModel.onStatusFilterChanged(OrderStatus.PAID)
                            }

                            3 -> {
                                orderViewModel.onStatusFilterChanged(OrderStatus.SHIPPED)
                            }

                            4 -> {
                                orderViewModel.onStatusFilterChanged(OrderStatus.CANCELLED)
                            }
                        }
                        orderViewModel.getGroupedOrders()
                        if(index != 0){
                            expandedGroups = state.allUsersOrders.keys.associateWith { true }
                        }
                    }
                )
                if (state.allUsersOrders.isEmpty()) {
                    EmptyPage("Oops!", "There is no order right now!")
                } else {
                    LaunchedEffect(state.allUsersOrders.keys) {
                        if (expandedGroups.isEmpty()) {
                            expandedGroups = state.allUsersOrders.keys.associateWith { false }
                        }
                    }
                    OrdersList(
                        lazyListState = lazyListState,
                        groupedOrders = state.allUsersOrders,
                        expandedGroups = expandedGroups,
                        onExpandedChange = { expandedGroups = it },
                        onCancelChangesClick = { orderViewModel.getGroupedOrders() },
                        onConfirmChangesClick = { changedOrders ->
                            orderViewModel.updateAllStatusesAndRefresh(changedOrders)
                            selectedChip = 0
                        }
                    )
                }
            } else if (userRole == UserRole.USER) {
                if (state.currentUserOrders.isEmpty()) {
                    EmptyPage("Oops!", "There is no order right now!")
                } else {
                    UserOrdersList(
                        lazyListState = lazyListState,
                        orderItems = state.currentUserOrders
                    )
                }
            }
        }
        alertMessage?.let {
            AlertModal(
                message = it,
                onConfirmClick = {
                    alertMessage = null
                }
            )
        }
    }
}