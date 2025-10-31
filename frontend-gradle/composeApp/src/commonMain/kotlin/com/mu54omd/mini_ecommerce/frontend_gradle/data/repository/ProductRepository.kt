package com.mu54omd.mini_ecommerce.frontend_gradle.data.repository

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult.*
import com.mu54omd.mini_ecommerce.frontend_gradle.api.map
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.PageResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import io.ktor.http.URLBuilder
import io.ktor.http.encodeURLParameter
import kotlin.collections.emptyList

class ProductRepository(private val api: ApiClient) {
    suspend fun getProducts(page: Int = 0, size: Int = 20): ApiResult<List<Product>> {
        return api.get<PageResponse<Product>>("/products?page=$page&size=$size")
            .map(
                onSuccess = {it.content}
            )
    }
    suspend fun searchProduct(query: String): ApiResult<List<Product>> {
        val encodedQuery = query.encodeURLParameter()
        return api.get<List<Product>>("/products/search?q=$encodedQuery")
            .map(
                onSuccess = { it }
            )
    }
}