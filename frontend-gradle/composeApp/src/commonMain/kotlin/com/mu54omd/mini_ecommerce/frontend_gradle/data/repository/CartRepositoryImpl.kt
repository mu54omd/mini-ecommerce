package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.CartRepository

class CartRepositoryImpl(private val api: ApiClient): CartRepository {

    override suspend fun addToCart(productId: Long, quantity: Int): ApiResult<CartResponse> {
        return api.post<String,CartResponse>("/cart/add?productId=$productId&quantity=$quantity","")
            .map(onSuccess = { it })
    }

    override suspend fun getCart(): ApiResult<CartResponse> {
        return api.get<CartResponse>("/cart")
            .map(
                onSuccess = { it }
            )
    }

    override suspend fun removeFromCart(productId: Long) {
        api.delete("/cart/remove/$productId")
    }

    override suspend fun clearCart() {
        api.delete("/cart/clear")
    }

    override suspend fun checkout(): ApiResult<String> {
        return api.post<String, String>("/cart/checkout", "")
            .map(onSuccess = {it})
    }
}