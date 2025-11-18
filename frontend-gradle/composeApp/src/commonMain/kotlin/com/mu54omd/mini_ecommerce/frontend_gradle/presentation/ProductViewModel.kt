package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.ImageUploadResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.ProductUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ProductViewModel(private val productUseCases: ProductUseCases) : ViewModel() {

    private val _productsState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val productsState = _productsState.asStateFlow()

    private val _addProductState = MutableStateFlow<UiState<Product>>(UiState.Idle)
    val addProductState = _addProductState.asStateFlow()

    private val _deleteProductState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteProductState = _deleteProductState.asStateFlow()

    private val _deactivateProduct = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deactivateProduct = _deactivateProduct.asStateFlow()

    private val _editProductState = MutableStateFlow<UiState<Product>>(UiState.Idle)
    val editProductState = _editProductState.asStateFlow()

    private val _uploadProductImageState = MutableStateFlow<UiState<ImageUploadResponse>>(UiState.Idle)
    val uploadProductImageState = _uploadProductImageState.asStateFlow()

    private val _searchQuery = MutableStateFlow<String?>(null)

    // ============================================================
    private var currentPage = 0
    private var isLastPage = false
    private var isPaginating = false
    private val pageSize = 20

    private val loadedProducts = mutableListOf<Product>()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    filterProducts(query =  query)
                }
        }
    }

    // ============================================================
        fun resetAllStates(){
        resetProductsState()
        resetAddProductState()
        resetEditProductState()
        resetDeleteProductState()
        resetUploadProductImageState()
    }

    fun resetProductsState(){
        _productsState.update { UiState.Idle }
    }

    fun resetAddProductState(){
        _addProductState.update { UiState.Idle }
    }

    fun resetEditProductState(){
        _editProductState.update { UiState.Idle }
    }

    fun resetDeleteProductState(){
        _deleteProductState.update { UiState.Idle }
    }

    fun resetUploadProductImageState(){
        _uploadProductImageState.update { UiState.Idle }
    }

    private fun resetPagination() {
        currentPage = 0
        isLastPage = false
        isPaginating = false
        loadedProducts.clear()
        _productsState.value = UiState.Loading
    }

    // ============================================================

    fun refreshProducts(){
        viewModelScope.launch {
            currentPage = 0
            isLastPage = false
            loadedProducts.clear()
            _productsState.update { UiState.Loading }
            loadNextPage()
        }
    }

    fun loadNextPage() {
        if (isPaginating || isLastPage) return
        viewModelScope.launch {
            isPaginating = true

            when (val result = productUseCases.getProductsUseCase(currentPage, pageSize)) {
                is ApiResult.Success -> {
                    val newProducts = result.data
                    if (newProducts.isEmpty()) isLastPage = true
                    else {
                        loadedProducts.addAll(newProducts)
                        currentPage++
                        _productsState.update { UiState.Success(loadedProducts.toList()) }
                    }
                }
                else -> { _productsState.update { result.toUiState() }}
            }
            isPaginating = false
        }
    }

    fun filterProducts(query: String?){
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                refreshProducts()
                return@launch
            }
            isLastPage = true
            isPaginating = false
            val result = productUseCases.searchProductsUseCase(query)
            _productsState.value = result.toUiState()
            println("state in filterProducts(): ${_productsState.value}")
        }
    }
    fun setSearchQuery(query: String?){
        _searchQuery.update { query }
    }

    fun addProduct(product: Product){
        viewModelScope.launch {
            _addProductState.update { UiState.Loading }
            val result = productUseCases.addProductUseCase(product)
            _addProductState.update { result.toUiState() }
            if(addProductState.value is UiState.Success){
                refreshProducts()
            }
        }
    }

    fun deleteProduct(productId: Long){
        viewModelScope.launch {
            _deleteProductState.update { UiState.Loading }
            val result = productUseCases.deleteProductUseCase(productId)
            _deleteProductState.update { result.toUiState() }
            refreshProducts()
        }
    }

    fun deactivateProduct(productId: Long){
        viewModelScope.launch {
            _deactivateProduct.update { UiState.Loading }
            val result = productUseCases.deactivateProductUseCase(productId)
            _deactivateProduct.update { result.toUiState() }
            refreshProducts()
        }
    }

    fun editProduct(product: Product){
        viewModelScope.launch {
            _editProductState.update { UiState.Loading }
            val result = productUseCases.editProductUseCase(product)
            _editProductState.update { result.toUiState() }
            if(editProductState.value is UiState.Success){
                refreshProducts()
            }
        }
    }

    fun uploadProductImage(productId: Long, fileName: String, byteArray: ByteArray){
        viewModelScope.launch {
            _uploadProductImageState.update { UiState.Loading }
            val result = productUseCases.uploadProductImageUseCase(productId, fileName, byteArray)
            _uploadProductImageState.update { result.toUiState() }
            if(uploadProductImageState.value is UiState.Success){
                refreshProducts()
            }
        }
    }

}