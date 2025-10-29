package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String = "guest",
    val role: String = "GUEST"
)