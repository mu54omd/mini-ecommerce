package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.CartRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    private val _cartState = MutableStateFlow<UiState<CartResponse>>(UiState.Idle)
    val cartState = _cartState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.Idle
        )

    private val _checkoutState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val checkoutState = _checkoutState.asStateFlow()

    fun reset(){
        _cartState.update { UiState.Idle }
    }
    fun refresh() {
        viewModelScope.launch {
            _cartState.update { UiState.Loading }
            val result = repo.getCart()
            _cartState.update { result.toUiState()}
        }
    }

    fun add(productId: Long, qty: Int = 1) {
        viewModelScope.launch {
            repo.addToCart(productId, qty)
            refresh()
        }
    }
    fun remove(productId: Long) {
        viewModelScope.launch {
            repo.removeFromCart(productId)
            refresh()
        }
    }
    fun clear() {
        viewModelScope.launch {
            repo.clearCart()
            refresh()
        }
    }

    fun checkout() {
        viewModelScope.launch {
            _checkoutState.update { UiState.Loading }
            val result = repo.checkout()
            _checkoutState.update { result.toUiState() }
        }
    }
}