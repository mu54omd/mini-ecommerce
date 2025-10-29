package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.ProductRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val products = _products.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UiState.Idle
    )

    fun reset(){
        _products.update { UiState.Idle }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _products.update { UiState.Loading }
            val result = repo.getProducts()
            _products.update { result.toUiState() }
        }
    }
}