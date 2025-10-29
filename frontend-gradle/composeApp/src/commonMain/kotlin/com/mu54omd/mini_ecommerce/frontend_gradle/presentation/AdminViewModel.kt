package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(private val repo: AdminRepository): ViewModel() {

    public fun addProduct(product: Product){
        viewModelScope.launch {
            repo.addProduct(product)
        }
    }

    public fun deleteProduct(productId: Long){
        viewModelScope.launch {
            repo.deleteProduct(productId)
        }
    }

    public fun updateProductStock(productId: Long, stock: Int){
        viewModelScope.launch {
            repo.updateProductStock(productId, stock)
        }
    }
}