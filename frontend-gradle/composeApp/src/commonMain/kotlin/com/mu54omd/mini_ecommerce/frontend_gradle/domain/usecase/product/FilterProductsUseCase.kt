package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository

class FilterProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(query: String?, category: String?, page: Int = 0, size: Int = 20): ApiResult<List<Product>> {
        return productRepository.filterProducts(query, category, page, size)
    }
}