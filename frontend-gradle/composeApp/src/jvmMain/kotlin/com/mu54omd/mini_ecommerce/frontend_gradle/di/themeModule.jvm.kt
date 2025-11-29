package com.mu54omd.mini_ecommerce.frontend_gradle.di

import com.mu54omd.mini_ecommerce.frontend_gradle.preference.ThemeStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual val themeModule: Module = module {
    single { ThemeStorage() }
}