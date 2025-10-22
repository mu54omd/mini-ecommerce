package com.mu54omd.mini_ecommerce.frontend_gradle.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)

@Serializable
data class JwtResponse(
    val token: String
)