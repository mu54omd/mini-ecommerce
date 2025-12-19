package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(
    val id: Long,
    val response: String
)
