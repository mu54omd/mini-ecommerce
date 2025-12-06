package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository

class GetCategoriesUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): ApiResult<List<String>> {
        return productRepository.getCategories()
    }
}