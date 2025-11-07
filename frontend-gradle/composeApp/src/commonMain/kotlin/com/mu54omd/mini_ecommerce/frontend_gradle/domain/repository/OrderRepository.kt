package com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse

interface OrderRepository {
    suspend fun getOrders(): ApiResult<List<OrderResponse>>
    suspend fun getUserOrders(): ApiResult<List<OrderResponse>>
    suspend fun updateOrderStatus(orderId: Long, status: String): ApiResult<OrderResponse>
}