package com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.product

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ImageUploadResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.permission.PermissionChecker
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository

class UploadProductImageUseCase(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val permissionChecker: PermissionChecker
) {
    suspend operator fun invoke(productId: Long, fileName: String, byteArray: ByteArray): ApiResult<ImageUploadResponse> {
        val user = authRepository.getUserInfo()
        if (!permissionChecker.canEditProduct(user)) ApiResult.Unauthorized()
        return productRepository.uploadProductImage(productId, fileName, byteArray)
    }
}