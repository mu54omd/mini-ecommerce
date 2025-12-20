package com.mu54omd.mini_ecommerce.frontend_gradle.di

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.AuthRepositoryImpl
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.CartRepositoryImpl
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.OrderRepositoryImpl
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.ProductRepositoryImpl
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.UserRepositoryImpl
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission.PermissionChecker
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.CartRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.OrderRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.UserRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.AuthUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.CartUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.OrderUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.ProductUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.UserUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.CheckHealthUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.ClearTokenUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.GetUserInfoUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.LoginUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.LogoutUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.RegisterUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.auth.ValidateTokenUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.AddToCartUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.CheckoutUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.ClearCartUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.GetCartUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.RemoveFromCartUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order.GetOrdersUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order.GetUserOrderUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order.SearchOrdersUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.order.UpdateOrderStatusUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.AddProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.DeactivateProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.DeleteProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.EditProductUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.FilterProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetCategoriesUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetLatestProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetProductsByCategoryUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.GetProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.SearchProductsUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product.UploadProductImageUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user.CreateUserUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user.DeleteUserUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user.EditUserUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.user.GetAllUsersUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.network.createHttpClient
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.AuthViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.CartViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.OrderViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.ProductViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.presentation.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    //Http Client
    single { createHttpClient() }

    // Api
    single { ApiClient(get()) }

    // Permission Checker
    single { PermissionChecker() }

    // Repo
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }

    // Auth Use Cases
    single { ClearTokenUseCase(get()) }
    single { CheckHealthUseCase(get()) }
    single { GetUserInfoUseCase(get()) }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { ValidateTokenUseCase(get()) }

    single { AuthUseCases(get(), get(),get(), get(), get(), get(), get()) }

    // Cart Use Cases
    single { AddToCartUseCase(get(), get(), get()) }
    single { CheckoutUseCase(get(), get(), get()) }
    single { ClearCartUseCase(get(), get(), get()) }
    single { GetCartUseCase(get()) }
    single { RemoveFromCartUseCase(get(), get(), get()) }

    single { CartUseCases(get(), get(), get(), get(), get()) }

    // Order Use Cases
    single { GetOrdersUseCase(get(), get(), get()) }
    single { GetUserOrderUseCase(get(), get(), get()) }
    single { UpdateOrderStatusUseCase(get(), get(), get()) }
    single { SearchOrdersUseCase(get(), get(), get()) }

    single { OrderUseCases(get(), get(), get(), get()) }

    // Product Use Cases
    single { AddProductUseCase(get(), get(), get()) }
    single { DeleteProductUseCase(get(), get(), get()) }
    single { DeactivateProductUseCase(get(), get(), get()) }
    single { EditProductUseCase(get(), get(), get()) }
    single { GetProductsUseCase(get()) }
    single { GetLatestProductsUseCase(get()) }
    single { GetCategoriesUseCase(get()) }
    single { GetProductsByCategoryUseCase(get()) }
    single { SearchProductsUseCase(get()) }
    single { FilterProductsUseCase(get()) }
    single { UploadProductImageUseCase(get(), get(), get()) }

    single { ProductUseCases(get(),get(),get(), get(), get(), get(), get(),get(), get(), get(), get()) }

    // Order Use Cases
    single { DeleteUserUseCase(get(), get(), get()) }
    single { CreateUserUseCase(get(), get(), get()) }
    single { EditUserUseCase(get(), get(), get()) }
    single { GetAllUsersUseCase(get(), get(), get()) }

    single { UserUseCases(get(),get(), get(), get()) }

    // ViewModels
    viewModel { ProductViewModel(get()) }
    viewModel { CartViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { OrderViewModel(get(), get()) }
}