package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository

class SearchProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(query: String): ApiResult<List<Product>> {
        return productRepository.searchProduct(query)
    }
}