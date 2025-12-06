package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository

class GetLatestProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(page: Int = 0, size: Int = 5): ApiResult<List<Product>> {
        return productRepository.getLatestProducts(page, size)
    }
}