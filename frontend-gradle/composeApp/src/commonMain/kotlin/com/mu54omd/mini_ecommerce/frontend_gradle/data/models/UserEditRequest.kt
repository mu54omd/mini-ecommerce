package com.mu54omd.mini_ecommerce.frontend_gradle.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserEditRequest(
    val username: String,
    val password: String,
    val email: String,
    val role: String,
)
