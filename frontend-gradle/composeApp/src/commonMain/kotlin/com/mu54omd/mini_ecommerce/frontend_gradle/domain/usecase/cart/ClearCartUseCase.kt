package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission.PermissionChecker
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.CartRepository

class ClearCartUseCase(
    private val authRepository: AuthRepository,
    private val cartRepository: CartRepository,
    private val permissionChecker: PermissionChecker
) {
    suspend operator fun invoke() {
        val user = authRepository.getUserInfo()
        if (!permissionChecker.canPlaceOrder(user)) ApiResult.Unauthorized()
        cartRepository.clearCart()
    }
}