package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission.PermissionChecker
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.CartRepository

class AddToCartUseCase(
    private val authRepository: AuthRepository,
    private val cartRepository: CartRepository,
    private val permissionChecker: PermissionChecker
) {
    suspend operator fun invoke(productId: Long, quantity: Int): ApiResult<CartResponse> {
        val user = authRepository.getUserInfo()
        if (!permissionChecker.canPlaceOrder(user)) ApiResult.Unauthorized()
        return cartRepository.addToCart(productId, quantity)
    }
}