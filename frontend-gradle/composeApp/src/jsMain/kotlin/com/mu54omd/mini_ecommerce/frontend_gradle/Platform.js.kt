package com.mu54omd.mini_ecommerce.frontend_gradle

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()