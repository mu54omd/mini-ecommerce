package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.OrderUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(private val orderUseCases: OrderUseCases): ViewModel() {
    private val _userOrderState = MutableStateFlow<UiState<List<OrderResponse>>>(UiState.Idle)
    val userOrdersState = _userOrderState.asStateFlow()

    private val _ordersState = MutableStateFlow<UiState<List<OrderResponse>>>(UiState.Idle)
    val ordersState = _ordersState.asStateFlow()

    private val _orderStatusState = MutableStateFlow<UiState<OrderResponse>>(UiState.Idle)
    val orderStatusState = _orderStatusState.asStateFlow()

    // ==============================================================================
    fun resetAllStates(){
        resetUserOrderState()
        resetOrdersState()
    }

    fun resetUserOrderState(){
        _userOrderState.update { UiState.Idle }
    }
    fun resetOrdersState(){
        _ordersState.update { UiState.Idle }
    }

    // ==============================================================================

    fun getUserOrders() {
        viewModelScope.launch {
            _userOrderState.update { UiState.Loading }
            val result = orderUseCases.getUserOrderUseCase()
            _userOrderState.update { result.toUiState()}
        }
    }

    fun getAllOrders(){
        viewModelScope.launch {
            _ordersState.update { UiState.Idle }
            val result = orderUseCases.getOrdersUseCase()
            _ordersState.update { result.toUiState() }
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: String){
        viewModelScope.launch {
            _orderStatusState.update { UiState.Loading }
            val result = orderUseCases.updateOrderStatusUseCase(orderId, newStatus)
            _orderStatusState.update { result.toUiState() }
        }
    }
}