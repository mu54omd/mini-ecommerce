package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission.PermissionChecker
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository

class EditProductUseCase(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val permissionChecker: PermissionChecker
) {
    suspend operator fun invoke(product: Product) : ApiResult<Product> {
        val user = authRepository.getUserInfo()
        if (!permissionChecker.canEditProduct(user)) return ApiResult.Unauthorized()
        return productRepository.editProduct(product)
    }
}