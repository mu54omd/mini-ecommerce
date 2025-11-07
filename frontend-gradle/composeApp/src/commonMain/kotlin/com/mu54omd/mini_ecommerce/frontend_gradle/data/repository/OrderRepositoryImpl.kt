package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.OrderRepository

class OrderRepositoryImpl(private val api: ApiClient): OrderRepository {

    override suspend fun getOrders(): ApiResult<List<OrderResponse>> = api.get("/orders")

    override suspend fun getUserOrders(): ApiResult<List<OrderResponse>> = api.get("/orders/myOrders")

    override suspend fun updateOrderStatus(orderId: Long, status: String): ApiResult<OrderResponse> =
        api.put("/orders/status/$orderId?status=$status", "")
}