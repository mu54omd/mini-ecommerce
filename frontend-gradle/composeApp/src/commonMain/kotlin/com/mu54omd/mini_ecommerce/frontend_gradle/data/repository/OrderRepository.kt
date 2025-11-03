package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse

class OrderRepository(private val api: ApiClient) {
    suspend fun getMyOrders(): ApiResult<List<OrderResponse>> = api.get("/orders/myOrders")
}