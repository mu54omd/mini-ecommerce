package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long? = null,
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val imageUrl: String? = null
)
