package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.CartUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(private val cartUseCases: CartUseCases) : ViewModel() {

    private val _cartState = MutableStateFlow<UiState<CartResponse>>(UiState.Idle)
    val cartState = _cartState.asStateFlow()

    private val _checkoutState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val checkoutState = _checkoutState.asStateFlow()

    private val _cartItems = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val cartItems = _cartItems.asStateFlow()

    // ============================================================
    fun resetAllStates(){
        resetCartState()
        resetCheckoutState()
    }

    fun resetCartState(){
        _cartState.update { UiState.Idle }
    }

    fun resetCheckoutState(){
        _checkoutState.update { UiState.Idle }
    }

    // ============================================================

    fun getCart() {
        viewModelScope.launch {
            _cartState.update { UiState.Loading }
            val result = cartUseCases.getCartUseCase()
            _cartState.update { result.toUiState() }

            if (cartState.value is UiState.Success) {
                syncCartItems((cartState.value as UiState.Success<CartResponse>).data)
            }
        }
    }

    private fun syncCartItems(response: CartResponse){
        _cartItems.value = response.items.associate { it.product.id to it.quantity }
    }

    fun add(productId: Long, qty: Int = 1) {
        viewModelScope.launch {
            cartUseCases.addToCartUseCase(productId, qty)
            getCart()
        }
    }
    fun remove(productId: Long) {
        viewModelScope.launch {
            cartUseCases.removeFromCartUseCase(productId)
            getCart()
        }
    }
    fun clear() {
        viewModelScope.launch {
            cartUseCases.clearCartUseCase()
            getCart()
        }
    }

    fun checkout() {
        viewModelScope.launch {
            _checkoutState.update { UiState.Loading }
            val result = cartUseCases.checkoutUseCase()
            _checkoutState.update { result.toUiState() }
            if(_checkoutState.value is UiState.Success){
                getCart()
            }
        }
    }
}