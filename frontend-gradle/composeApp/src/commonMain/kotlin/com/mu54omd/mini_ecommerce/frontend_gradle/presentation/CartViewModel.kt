package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartItemResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.CartUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(private val cartUseCases: CartUseCases) : ViewModel() {

    private val _state = MutableStateFlow(CartUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CartUiEffect>()
    val effect = _effect.asSharedFlow()

    // ===================== Initial Load =====================
    fun loadCart() {
        viewModelScope.launch {
            _state.update { it.copy(isInitialLoading = true) }

            when (val result = cartUseCases.getCartUseCase()) {
                is ApiResult.Success -> {
                    val items = result.data.items
                    val itemsCounts = items.associate { it.product.id to it.quantity }
                    _state.update {
                        it.copy(
                            id = result.data.id,
                            items = items,
                            itemsCount = itemsCounts,
                            isEmpty = items.isEmpty(),
                            isInitialLoading = false,
                            isRefreshing = false
                        )
                    }
                }
                is ApiResult.NetworkError -> {
                    _state.update { it.copy(isInitialLoading = false) }
                    _effect.emit(CartUiEffect.ShowError(result.exception.message))
                }
                else -> {
                    _state.update { it.copy(isInitialLoading = false) }
                    _effect.emit(CartUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }

    // ===================== Cart Actions =====================
    fun add(productId: Long) = updateCart {
        cartUseCases.addToCartUseCase(productId, 1)
    }

    fun remove(productId: Long) = updateCart {
        cartUseCases.removeFromCartUseCase(productId)
    }

    fun clear() = updateCart {
        cartUseCases.clearCartUseCase()
    }

    private fun updateCart(action: suspend () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            action()
            loadCart()
        }
    }

    // ===================== Checkout =====================
    fun checkout() {
        viewModelScope.launch {
            _state.update { it.copy(isCheckingOut = true) }

            when (val result = cartUseCases.checkoutUseCase()) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isCheckingOut = false) }
                    _effect.emit(CartUiEffect.CheckoutSuccess(id = result.data.id, message = result.data.response))
                    loadCart()
                }
                is ApiResult.NetworkError -> {
                    _state.update { it.copy(isCheckingOut = false) }
                    _effect.emit(CartUiEffect.ShowError(result.exception.message))
                }

                else -> {
                    _state.update { it.copy(isCheckingOut = false) }
                    _effect.emit(CartUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }
}

data class CartUiState(
    val id: Long = 0,
    val items: List<CartItemResponse> = emptyList(),
    val itemsCount: Map<Long, Int> = emptyMap(),
    val isInitialLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isCheckingOut: Boolean = false,
    val isEmpty: Boolean = false
)

sealed interface CartUiEffect {
    data class ShowError(val message: String?) : CartUiEffect
    data class CheckoutSuccess(val id: Long, val message: String) : CartUiEffect
}