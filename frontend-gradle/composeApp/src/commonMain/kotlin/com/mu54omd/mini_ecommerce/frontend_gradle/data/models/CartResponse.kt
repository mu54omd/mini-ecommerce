package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val id: Long,
    val items: List<CartItemResponse> = emptyList()
)