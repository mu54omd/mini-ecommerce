package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: Double
)
