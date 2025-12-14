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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(FlowPreview::class)
class ProductViewModel(private val productUseCases: ProductUseCases) : ViewModel() {

    private val _productsState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val productsState = _productsState.asStateFlow()

    private val _categoriesState = MutableStateFlow<UiState<List<String>>>(UiState.Idle)
    val categoriesState = _categoriesState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _latestProductsBannerState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val latestProductsBannerState = _latestProductsBannerState.asStateFlow()

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
    private val paginationMutex = Mutex()


    private val loadedProducts = mutableListOf<Product>()

    init {
        getLatestProductsBanner()
        getCategories()
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    filterProducts(query =  query, category = selectedCategory.value)
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

    fun resetLatestProductsBannerState(){
        _latestProductsBannerState.update { UiState.Idle }
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
            paginationMutex.withLock {
                resetPagination()
                loadNextPage()
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            val result = productUseCases.getCategoriesUseCase()
            _categoriesState.value = result.toUiState()
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        refreshProducts()
    }

    fun loadNextPage() {
        viewModelScope.launch {
            paginationMutex.withLock {
                if (isLastPage) return@withLock
                isPaginating = true

                val category = _selectedCategory.value
                val result = if (category.isNullOrEmpty()) {
                    productUseCases.getProductsUseCase(currentPage, pageSize)
                } else {
                    productUseCases.getProductsByCategoryUseCase(category, currentPage, pageSize)
                }
                when (result) {
                    is ApiResult.Success -> {
                        val newProducts = result.data
                        if (newProducts.isEmpty()) {
                            isLastPage = true
                        } else {
                            loadedProducts.addAll(newProducts)
                            currentPage++
                            _productsState.update { UiState.Success(loadedProducts.toList()) }
                        }
                        if (loadedProducts.isEmpty() && isLastPage) {
                            _productsState.update { result.toUiState() }
                        }
                    }
                    else -> {
                        _productsState.update { result.toUiState() }
                    }
                }
                isPaginating = false
            }
        }
    }

    fun getLatestProductsBanner() {
        viewModelScope.launch {
            _latestProductsBannerState.update { UiState.Loading }
            val result = productUseCases.getLatestProductsUseCase(0, 5)
            _latestProductsBannerState.update { result.toUiState() }
        }
    }

    fun setSearchQuery(query: String?){
        _searchQuery.update { query }
    }
    fun searchProducts(query: String?){
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                refreshProducts()
                return@launch
            }
            isLastPage = true
            isPaginating = false
            val result = productUseCases.searchProductsUseCase(query)
            _productsState.value = result.toUiState()
        }
    }

    fun filterProducts(query: String?, category: String?, page: Int = 0, size: Int = 20){
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                refreshProducts()
                return@launch
            }
            isLastPage = true
            isPaginating = false
            val result = productUseCases.filterProductsUseCase(query, category, page, size)
            _productsState.value = result.toUiState()
        }
    }

    fun addProduct(product: Product){
        viewModelScope.launch {
            _addProductState.update { UiState.Loading }
            val result = productUseCases.addProductUseCase(product)
            _addProductState.update { result.toUiState() }
            if(result is ApiResult.Success){
                refreshProducts()
            }
        }
    }

    fun editProduct(product: Product){
        viewModelScope.launch {
            _editProductState.update { UiState.Loading }
            val result = productUseCases.editProductUseCase(product)
            _editProductState.update { result.toUiState() }
            if(result is ApiResult.Success){
                refreshProducts()
            }
        }
    }

    fun uploadProductImage(productId: Long, fileName: String, byteArray: ByteArray){
        viewModelScope.launch {
            _uploadProductImageState.update { UiState.Loading }
            val result = productUseCases.uploadProductImageUseCase(productId, fileName, byteArray)
            _uploadProductImageState.update { result.toUiState() }
        }
    }


//    fun deleteProduct(productId: Long){
//        viewModelScope.launch {
//            _deleteProductState.update { UiState.Loading }
//            val result = productUseCases.deleteProductUseCase(productId)
//            _deleteProductState.update { result.toUiState() }
//            refreshProducts()
//        }
//    }

    fun deactivateProduct(productId: Long){
        viewModelScope.launch {
            _deactivateProduct.update { UiState.Loading }
            val result = productUseCases.deactivateProductUseCase(productId)
            _deactivateProduct.update { result.toUiState() }
            refreshProducts()
        }
    }

}