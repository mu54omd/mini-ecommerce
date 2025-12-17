package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ImageUploadResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.PageResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.repository.ProductRepository
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeURLParameter

class ProductRepositoryImpl(private val api: ApiClient): ProductRepository {
   override suspend fun getProducts(page: Int, size: Int): ApiResult<List<Product>> {
        return api.get<PageResponse<Product>>("/products?page=$page&size=$size")
            .map(
                onSuccess = {it.content}
            )
    }

    override suspend fun getLatestProducts(page: Int, size: Int): ApiResult<List<Product>> {
        return api.get<PageResponse<Product>>("/products/latest?page=$page&size=$size")
            .map(
                onSuccess = { it.content }
            )
    }

    override suspend fun getCategories(): ApiResult<List<String>> {
        return api.get<List<String>>("/products/categories")
            .map(
                onSuccess = { it }
            )
    }

    override suspend fun getProductsByCategory(category: String, page: Int, size: Int): ApiResult<List<Product>> {
        val encodedCategory = category.encodeURLParameter()
        return api.get<PageResponse<Product>>("/products/categories/$encodedCategory?page=$page&size=$size")
            .map(
                onSuccess = { it.content }
            )
    }

    override suspend fun searchProduct(query: String): ApiResult<List<Product>> {
        val encodedQuery = query.encodeURLParameter()
        return api.get<List<Product>>("/products/search?q=$encodedQuery")
            .map(
                onSuccess = { it }
            )
    }

    override suspend fun filterProducts(
        query: String?,
        category: String?,
        page: Int,
        size: Int
    ): ApiResult<List<Product>> {
        val params = mutableMapOf<String, String>()
        query?.let { params["q"] = it.encodeURLParameter() }
        category?.let { params["category"] = it.encodeURLParameter() }
        val query = params.entries.joinToString("&") { "${it.key}=${it.value}" }
        val url = if (params.isEmpty()) "/products?page=$page&size=$size" else "/products/filter?$query&page=$page&size=$size"
        return api.get<PageResponse<Product>>(url)
            .map(
                onSuccess = { it.content }
            )
    }

    override suspend fun addProduct(product: Product): ApiResult<Product> = api.post("/products", product)
    override suspend fun uploadProductImage(
        productId: Long,
        fileName: String,
        byteArray: ByteArray
    ): ApiResult<ImageUploadResponse> {
        return api.postMultipart(
            path = "/products/$productId/upload-image",
            body = MultiPartFormDataContent(
                formData {
                    append("file", byteArray, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=$fileName"
                        )
                    }
                    )
                }
            )
        )
    }
    override suspend fun editProduct(product: Product): ApiResult<Product>{
        return api.put("/products", product)
    }

    override suspend fun updateProductStock(id: Long, stock: Int): ApiResult<Product> =
        api.put("/products/$id/stock?stock=$stock", "")

    override suspend fun deleteProduct(id: Long): ApiResult<Unit> = api.delete("/products?productId=$id")
    override suspend fun deactivateProduct(id: Long): ApiResult<Unit> = api.patch("/products?productId=$id")

}