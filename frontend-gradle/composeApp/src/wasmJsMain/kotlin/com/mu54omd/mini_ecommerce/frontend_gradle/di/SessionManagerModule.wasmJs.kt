package com.mu54omd.mini_ecommerce.frontend_gradle.di

import com.mu54omd.mini_ecommerce.frontend_gradle.storage.SessionManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val sessionManagerModule: Module = module {
    single { SessionManager() }
}