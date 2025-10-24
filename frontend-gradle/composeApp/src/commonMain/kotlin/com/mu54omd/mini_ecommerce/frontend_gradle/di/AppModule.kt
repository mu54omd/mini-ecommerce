package com.mu54omd.mini_ecommerce.frontend_gradle.di

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.CartRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.ProductRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.network.createHttpClient
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient() }
    // Api
    single { ApiClient(get(), get()) }

    // Repo
    single { ProductRepository(get()) }
    single { CartRepository(get()) }
    single { AuthRepository(get(), get()) }

    // ViewModels
    viewModel { ProductViewModel(get()) }
    viewModel { CartViewModel(get()) }
    viewModel { AuthViewModel(get()) }
}