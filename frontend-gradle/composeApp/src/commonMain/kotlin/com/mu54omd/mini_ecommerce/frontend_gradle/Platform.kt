package com.mu54omd.mini_ecommerce.frontend_gradle

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform