package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val id: Long,
    val username: String,
    val items: List<OrderItemResponse>,
    val totalPrice: Double,
    val status: String,
)