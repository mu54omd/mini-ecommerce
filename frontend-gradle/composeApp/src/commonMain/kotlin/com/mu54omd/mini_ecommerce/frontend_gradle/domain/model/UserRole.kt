package com.mu54omd.mini_ecommerce.frontend_gradle.domain.model

enum class UserRole {
    GUEST,
    USER,
    ADMIN;
    companion object{
        fun fromString(role: String?):UserRole {
           return when(role){
                "ADMIN" -> ADMIN
                "USER" -> USER
                else -> GUEST
            }
        }
    }
}