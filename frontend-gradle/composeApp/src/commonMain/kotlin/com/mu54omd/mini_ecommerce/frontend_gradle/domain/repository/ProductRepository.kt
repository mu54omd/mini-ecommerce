package com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ImageUploadResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.PageResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeURLParameter

interface ProductRepository {
    suspend fun getProducts(page: Int = 0, size: Int = 20): ApiResult<List<Product>>
    suspend fun searchProduct(query: String): ApiResult<List<Product>>

    suspend fun addProduct(product: Product): ApiResult<Product>
    suspend fun uploadProductImage(
        productId: Long,
        fileName: String,
        byteArray: ByteArray
    ): ApiResult<ImageUploadResponse>
    suspend fun editProduct(product: Product): ApiResult<Product>

    suspend fun updateProductStock(id: Long, stock: Int): ApiResult<Product>
    suspend fun deleteProduct(id: Long): ApiResult<Unit>
}