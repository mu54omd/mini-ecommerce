package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.OrderRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState.*
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(private val repo: OrderRepository): ViewModel() {
    private val _ordersState = MutableStateFlow<UiState<List<OrderResponse>>>(UiState.Idle)
    val ordersState: StateFlow<UiState<List<OrderResponse>>> = _ordersState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.Idle
        )
    fun load() {
        viewModelScope.launch {
            _ordersState.update { UiState.Loading }
            val result = repo.getMyOrders()
            _ordersState.update { result.toUiState()}
        }
    }
}