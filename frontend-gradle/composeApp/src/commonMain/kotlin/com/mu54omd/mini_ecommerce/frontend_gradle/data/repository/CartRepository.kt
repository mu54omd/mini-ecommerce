package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse

class CartRepository(private val api: ApiClient) {

    suspend fun addToCart(productId: Long, quantity: Int): CartResponse {
        return api.post<String,CartResponse>("/cart/add?productId=$productId&quantity=$quantity","")
    }

    suspend fun getCart(): CartResponse {
        return api.get("/cart")
    }

    suspend fun removeFromCart(productId: Long) {
        api.delete("/cart/remove?productId=$productId")
    }

    suspend fun clearCart() {
        api.delete("/cart/clear")
    }
}