package com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ImageUploadResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product

interface ProductRepository {
    suspend fun getProducts(page: Int = 0, size: Int = 20): ApiResult<List<Product>>

    suspend fun getLatestProducts(page: Int = 0, size: Int = 5): ApiResult<List<Product>>
    suspend fun getCategories(): ApiResult<List<String>>
    suspend fun getProductsByCategory(category: String, page: Int = 0, size: Int = 20): ApiResult<List<Product>>
    suspend fun searchProduct(query: String): ApiResult<List<Product>>
    suspend fun filterProducts(query: String?, category: String?, page: Int = 0, size: Int = 20): ApiResult<List<Product>>

    suspend fun addProduct(product: Product): ApiResult<Product>
    suspend fun uploadProductImage(
        productId: Long,
        fileName: String,
        byteArray: ByteArray
    ): ApiResult<ImageUploadResponse>
    suspend fun editProduct(product: Product): ApiResult<Product>

    suspend fun updateProductStock(id: Long, stock: Int): ApiResult<Product>
    suspend fun deleteProduct(id: Long): ApiResult<Unit>
    suspend fun deactivateProduct(id: Long): ApiResult<Unit>
}