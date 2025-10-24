package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.PageResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import kotlinx.serialization.json.Json

class ProductRepository(private val api: ApiClient) {
    suspend fun getProducts(page: Int = 0, size: Int = 20): List<Product> {
        val resp = api.get<PageResponse<Product>>("/products?page=$page&size=$size")
        // resp is JSON string — decode to PageResponse<Product>
        return resp.content
    }

}