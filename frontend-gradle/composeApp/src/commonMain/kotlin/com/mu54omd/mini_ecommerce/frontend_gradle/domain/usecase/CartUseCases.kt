package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase

import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.AddToCartUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.CheckoutUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.ClearCartUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.GetCartUseCase
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart.RemoveFromCartUseCase

data class CartUseCases(
    val addToCartUseCase: AddToCartUseCase,
    val checkoutUseCase: CheckoutUseCase,
    val clearCartUseCase: ClearCartUseCase,
    val getCartUseCase: GetCartUseCase,
    val removeFromCartUseCase: RemoveFromCartUseCase
)