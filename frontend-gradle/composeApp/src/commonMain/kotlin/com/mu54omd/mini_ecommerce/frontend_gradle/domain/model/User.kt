package com.mu54omd.mini_ecommerce.frontend_gradle.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String = "guest",
    val role: UserRole = UserRole.GUEST
)