package com.mu54omd.mini_ecommerce.frontend_gradle

import android.app.Application
import com.mu54omd.mini_ecommerce.frontend_gradle.di.initKoin
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.initKmp
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKmp(this@MainApplication)
        initKoin{
            androidContext(this@MainApplication)
        }
    }
}