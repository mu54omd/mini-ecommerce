package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.ProductRepository

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {
    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    suspend fun loadProducts() {
        products = repo.getProducts()
    }
}