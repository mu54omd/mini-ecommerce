package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.OrderUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(private val orderUseCases: OrderUseCases): ViewModel() {
    private val _userOrderState = MutableStateFlow<UiState<List<OrderResponse>>>(UiState.Idle)
    val userOrdersState = _userOrderState.asStateFlow()

    private val _ordersState = MutableStateFlow<UiState<List<OrderResponse>>>(UiState.Idle)
    val ordersState = _ordersState.asStateFlow()

    private val _groupedOrders = MutableStateFlow<UiState<Map<String, List<OrderResponse>>>>(UiState.Idle)
    val groupedOrders = _groupedOrders.asStateFlow()


    private val _updateStatusSummary = MutableStateFlow<List<UpdateOrderStatusResult>>(emptyList())
    val updateStatusSummary = _updateStatusSummary.asStateFlow()

    // ==============================================================================
    fun resetAllStates(){
        resetUserOrderState()
        resetOrdersState()
        resetGroupedOrders()
    }

    fun resetUserOrderState(){
        _userOrderState.update { UiState.Idle }
    }
    fun resetOrdersState(){
        _ordersState.update { UiState.Idle }
    }

    fun resetGroupedOrders(){
        _groupedOrders.update { UiState.Idle }
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

    fun getGroupedOrders(){
        viewModelScope.launch {
            _groupedOrders.update { UiState.Loading }
            val result = orderUseCases.getOrdersUseCase()
                .map(
                    onSuccess = { list -> list.groupBy { it.username } }
                )
            _groupedOrders.update { result.toUiState()}
        }
    }

    fun updateAllStatusesAndRefresh(changed: Map<Long, String>) {
        viewModelScope.launch {
            val results = mutableListOf<UpdateOrderStatusResult>()
            val jobs = changed.map { (orderId, newStatus) ->
                async {
                    val result = updateOrderStatusSuspended(orderId, newStatus)
                    results.add(UpdateOrderStatusResult(orderId, result.toUiState()))
                }
            }
            jobs.awaitAll()
            _updateStatusSummary.update { results }
            println(updateStatusSummary.value)
            getGroupedOrders()
        }
    }
    private suspend fun updateOrderStatusSuspended(
        orderId: Long, newStatus: String
    ): ApiResult<OrderResponse>{
        return orderUseCases.updateOrderStatusUseCase(orderId,newStatus)
    }
}

data class UpdateOrderStatusResult(
    val orderId: Long,
    val result: UiState<OrderResponse>,
)