package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CartItemResponse(
    val id: Long,
    val product: ProductResponse,
    val quantity: Int
)
