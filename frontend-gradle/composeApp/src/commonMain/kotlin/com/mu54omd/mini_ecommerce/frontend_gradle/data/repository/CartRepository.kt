package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse

class CartRepository(private val api: ApiClient) {

    suspend fun addToCart(productId: Long, quantity: Int): ApiResult<CartResponse> {
        return api.post<String,CartResponse>("/cart/add?productId=$productId&quantity=$quantity","")
            .map(onSuccess = { it })
    }

    suspend fun getCart(): ApiResult<CartResponse> {
        return api.get<CartResponse>("/cart")
            .map(
                onSuccess = { it }
            )
    }

    suspend fun removeFromCart(productId: Long) {
        api.delete("/cart/remove?productId=$productId")
    }

    suspend fun clearCart() {
        api.delete("/cart/clear")
    }

    suspend fun checkout() {
        api.post<String, String>("/cart/checkout", "")
    }
}