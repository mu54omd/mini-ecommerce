package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long = 0L,
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val role: String = "",
    val createdAt: String = ""
)
