package com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CheckoutResponse

interface CartRepository {
    suspend fun addToCart(productId: Long, quantity: Int): ApiResult<CartResponse>
    suspend fun getCart(): ApiResult<CartResponse>
    suspend fun removeFromCart(productId: Long)
    suspend fun clearCart()
    suspend fun checkout(): ApiResult<CheckoutResponse>
}