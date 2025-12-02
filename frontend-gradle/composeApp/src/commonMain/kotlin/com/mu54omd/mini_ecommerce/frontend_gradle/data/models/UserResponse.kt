package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long = 0L,
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val role: String = UserRole.USER.name,
    val createdAt: String = ""
)
