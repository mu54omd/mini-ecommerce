package com.mu54omd.mini_ecommerce.frontend_gradle.storage

import android.content.Context


private lateinit var appContext: Context

fun initKmp(context: Context) {
    appContext = context.applicationContext
}
actual fun getSessionManager(): SessionManager = SessionManager(appContext)