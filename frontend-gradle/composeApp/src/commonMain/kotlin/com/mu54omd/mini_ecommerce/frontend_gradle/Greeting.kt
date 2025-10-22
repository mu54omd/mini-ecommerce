package com.mu54omd.mini_ecommerce.frontend_gradle

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}