package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(category: String, page: Int = 0, size: Int = 20): ApiResult<List<Product>> {
        return productRepository.getProductsByCategory(category,page, size)
    }
}