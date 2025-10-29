package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product

class AdminRepository(private val api: ApiClient) {
    suspend fun getAllProducts(): ApiResult<List<Product>> = api.get("/products")
    suspend fun addProduct(product: Product): ApiResult<Product> = api.post("/products", product)
    suspend fun updateProductStock(id: Long, stock: Int): ApiResult<Product> = api.put("/products/$id/stock?stock=$stock", "")
    suspend fun deleteProduct(id: Long): ApiResult<Unit> = api.delete("/products?productId=$id")
    suspend fun getOrders(): ApiResult<List<OrderResponse>> = api.get("/orders")
    suspend fun updateOrderStatus(id: Long, status: String): ApiResult<OrderResponse> = api.put("/api/orders/$id/status?status=$status", "")
}