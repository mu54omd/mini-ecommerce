package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.cart

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.CartResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.CartRepository

class GetCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(): ApiResult<CartResponse> {
        return cartRepository.getCart()
    }
}