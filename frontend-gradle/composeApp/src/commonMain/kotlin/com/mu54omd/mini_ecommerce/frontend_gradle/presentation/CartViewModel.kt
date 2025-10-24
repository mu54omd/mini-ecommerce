package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    val cartState = mutableStateOf<CartResponse?>(null)
    val loading = mutableStateOf(false)

    fun refresh(scope: CoroutineScope = CoroutineScope(Dispatchers.Main)) {
        loading.value = true
        scope.launch {
            try {
                cartState.value = repo.getCart()
            } catch (e: Exception) {
                // handle
            } finally { loading.value = false }
        }
    }

    fun add(productId: Long, qty: Int = 1, scope: CoroutineScope = CoroutineScope(Dispatchers.Main)) {
        scope.launch {
            try {
                repo.addToCart(productId, qty)
                refresh(scope)
            } catch (e: Exception) {
                // handle
            }
        }
    }
    fun clear(scope: CoroutineScope = CoroutineScope(Dispatchers.Main)) {
        scope.launch {
            repo.clearCart()
            refresh(scope)
        }
    }
}