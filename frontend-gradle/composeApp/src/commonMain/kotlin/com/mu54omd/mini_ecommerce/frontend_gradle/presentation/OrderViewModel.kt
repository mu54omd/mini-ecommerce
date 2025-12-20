package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.AuthUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.OrderUseCases
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class OrderViewModel(
    private val authUseCases: AuthUseCases,
    private val orderUseCases: OrderUseCases
): ViewModel() {

    private val _state = MutableStateFlow(OrderUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OrderUiEffect>()
    val effect = _effect.asSharedFlow()


    // ==============================================================================

    init {
        viewModelScope.launch {
            val user =  authUseCases.getUserInfoUseCase()
            _state
                .map { it.searchQuery to it.statusFilter }
                .debounce(300)
                .distinctUntilChanged()
                .collect {
                   when(user.role){
                       UserRole.ADMIN -> { getGroupedOrders() }
                       else -> {}
                   }
                }
        }
    }
    // ==============================================================================

    fun getUserOrders() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            val s = _state.value
            val result = when {
                !s.searchQuery.isNullOrBlank() || !s.statusFilter.isNullOrBlank() -> {
                    orderUseCases.searchOrdersUseCase(
                        status = s.statusFilter,
                        productName = s.searchQuery
                    )
                }
                else -> {
                    orderUseCases.getUserOrderUseCase()
                }
            }

            when(result){
                is ApiResult.Success -> {
                    _state.update { it.copy( currentUserOrders = result.data, isRefreshing = false) }
                }
                else -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(OrderUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }

    fun getGroupedOrders(){
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            val s = _state.value
            val result = when{
                !s.searchQuery.isNullOrBlank() || !s.statusFilter.isNullOrBlank() -> {
                    orderUseCases.searchOrdersUseCase(
                        status = s.statusFilter,
                        productName = s.searchQuery
                    ).map(onSuccess = { list -> list.groupBy { it.username }})
                }
                else -> {
                    orderUseCases.getOrdersUseCase()
                        .map(onSuccess = { list -> list.groupBy { it.username } })
                }
            }

            when(result){
                is ApiResult.Success -> {
                    _state.update { it.copy( allUsersOrders = result.data, isRefreshing = false) }
                }
                else -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(OrderUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }



    fun onStatusFilterChanged(status: OrderStatus?){
        _state.update { it.copy(isRefreshing = true, statusFilter = status?.name) }
    }

    fun onSearchQueryChanged(query: String?){
        _state.update { it.copy(isRefreshing = true, searchQuery = query) }
    }

    fun updateAllStatusesAndRefresh(changed: Map<Long, String>) {
        viewModelScope.launch {
            val results = mutableListOf<UpdateOrderStatusResult>()
            val jobs = changed.map { (orderId, newStatus) ->
                async {
                    when(val result = updateOrderStatusSuspended(orderId, newStatus)){
                        is ApiResult.Success -> {
                            results.add(UpdateOrderStatusResult(orderId, null))
                        }
                        else -> {
                            results.add(UpdateOrderStatusResult(orderId, (result as ApiResult.Error).message))
                        }
                    }
                }
            }
            jobs.awaitAll()
            _state.update { it.copy(updateStatusSummary = results) }
            if(_state.value.updateStatusSummary.isNotEmpty()){
                val succeedAttempt = _state.value.updateStatusSummary.count { it.error == null }
                val failedAttempt = _state.value.updateStatusSummary.count { it.error != null }
                _effect.emit(OrderUiEffect.ShowMessage("$succeedAttempt order(s): Updated - $failedAttempt order(s): Failed"))
                _state.update { it.copy(updateStatusSummary = emptyList()) }
            }
            getGroupedOrders()
        }
    }


    private suspend fun updateOrderStatusSuspended(
        orderId: Long, newStatus: String
    ): ApiResult<OrderResponse>{
        return orderUseCases.updateOrderStatusUseCase(orderId,newStatus)
    }
}

data class OrderUiState(
    val currentUserOrders: List<OrderResponse> = emptyList(),
    val allUsersOrders: Map<String, List<OrderResponse>> = emptyMap(),
    val updateStatusSummary: List<UpdateOrderStatusResult> = emptyList(),
    val statusFilter: String? = null,
    val searchQuery: String? = null,
    val isRefreshing: Boolean = false,
)

sealed interface OrderUiEffect {
    data class ShowError(val message: String?): OrderUiEffect
    data class ShowMessage(val message: String?): OrderUiEffect
}

data class UpdateOrderStatusResult(
    val orderId: Long,
    val error: String?
)

enum class OrderStatus{
    CREATED,
    PAID,
    SHIPPED,
    CANCELLED
}