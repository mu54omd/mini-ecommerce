package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ImageUploadResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.PageResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeURLParameter

class AdminRepository(private val api: ApiClient) {

    //============= Users ===========
    suspend fun getAllUsers(): ApiResult<List<UserResponse>> = api.get("/users")
    suspend fun deleteUser(userId: Long): ApiResult<Unit> = api.delete("/users/${userId}")
    suspend fun editUser(userId: Long, user: UserEditRequest): ApiResult<UserResponse> =
        api.put("/users/${userId}", user)

    //============= Products ===========

    suspend fun getAllProducts(page: Int = 0, size: Int = 20): ApiResult<List<Product>> {
        return api.get<PageResponse<Product>>("/products?page=$page&size=$size")
            .map(
                onSuccess = { it.content }
            )

    }
    suspend fun searchProduct(query: String): ApiResult<List<Product>> {
        val encodedQuery = query.encodeURLParameter()
        return api.get<List<Product>>("/products/search?q=$encodedQuery")
            .map(
                onSuccess = { it }
            )
    }

    suspend fun addProduct(product: Product): ApiResult<Product> = api.post("/products", product)
    suspend fun uploadProductImage(
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
    suspend fun editProduct(product: Product): ApiResult<Product>{
        return api.put("/products", product)
    }

    suspend fun updateProductStock(id: Long, stock: Int): ApiResult<Product> =
        api.put("/products/$id/stock?stock=$stock", "")

    suspend fun deleteProduct(id: Long): ApiResult<Unit> = api.delete("/products?productId=$id")
    //============= Orders ===========

    suspend fun getOrders(): ApiResult<List<OrderResponse>> = api.get("/orders")
    suspend fun updateOrderStatus(orderId: Long, status: String): ApiResult<OrderResponse> =
        api.put("/orders/status/$orderId?status=$status", "")
}