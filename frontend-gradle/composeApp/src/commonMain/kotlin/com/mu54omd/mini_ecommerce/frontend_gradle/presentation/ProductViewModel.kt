package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.ProductUseCases
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(FlowPreview::class)
class ProductViewModel(
    private val productUseCases: ProductUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ProductsViewState())
    val state = _state.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20
    private val paginationMutex = Mutex()

    private var fetchJob: Job? = null

    init {
        loadInitialData()

        // Debounce search query
        viewModelScope.launch {
            _state
                .map { it.searchQuery to it.selectedCategory }
                .distinctUntilChanged()
                .debounce(300)
                .collect { reloadProducts() }
        }
    }

    // ======================== Initial Load ========================
    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, isInitialLoading = true) }

            val categoriesResult = productUseCases.getCategoriesUseCase()
            val bannerResult = productUseCases.getLatestProductsUseCase(0, 5)

            _state.update {
                it.copy(
                    categories = (categoriesResult as? ApiResult.Success)?.data ?: emptyList(),
                    banner = (bannerResult as? ApiResult.Success)?.data ?: emptyList()
                )
            }

            reloadProducts()
        }
    }

    // ======================== Events ========================

    fun onCategorySelected(category: String?) {
        _state.update {
            it.copy(
                selectedCategory = category,
                isLastPage = false
            )
        }
    }

    fun onSearchQueryChanged(query: String?) {
        _state.update {
            it.copy(
                searchQuery = query,
                isLastPage = false
            )
        }
    }

    fun refresh() {
        _state.update {
            it.copy(isLastPage = false)
        }
        reloadProducts()
    }

    fun loadNextPage() {
        fetchNextPage()
    }

    // ======================== CRUD & Side-effects ========================

    fun addProduct(product: Product, image: PlatformFile? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            val result = productUseCases.addProductUseCase(product)
            if (result is ApiResult.Success) {
                image?.let {
                    productUseCases.uploadProductImageUseCase(
                        result.data.id!!, it.name, it.readBytes()
                    )
                }
                _state.update { it.copy(message = "Product added") }
                reloadProducts()
            } else if (result is ApiResult.Error) {
                _state.update { it.copy(error = result.message) }
            }
        }
    }

    fun editProduct(product: Product, image: PlatformFile? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            val result = productUseCases.editProductUseCase(product)
            if (result is ApiResult.Success) {
                image?.let {
                    productUseCases.uploadProductImageUseCase(
                        product.id!!, it.name, it.readBytes()
                    )
                }
                _state.update { it.copy(message = "Product updated") }
                reloadProducts()
            } else if (result is ApiResult.Error) {
                _state.update { it.copy(error = result.message) }
            }
        }
    }

    fun deactivateProduct(productId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            val result = productUseCases.deactivateProductUseCase(productId)
            if (result is ApiResult.Success) {
                _state.update { it.copy(message = "Product deactivated") }
                reloadProducts()
            } else if (result is ApiResult.Error) {
                _state.update { it.copy(error = result.message) }
            }
        }
    }

    fun uploadProductImage(productId: Long, fileName: String, byteArray: ByteArray) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            val result = productUseCases.uploadProductImageUseCase(productId, fileName, byteArray)
            if (result is ApiResult.Success) {
                _state.update { it.copy(message = "Image uploaded successfully") }
            } else if (result is ApiResult.Error) {
                _state.update { it.copy(error = result.message) }
            }
        }
    }

    // ======================== Private Helpers ========================

    private fun reloadProducts() {
        currentPage = 0
        _state.update {
            it.copy(
                isRefreshing = true,
                isLastPage = false
            )
        }
        fetchNextPage(reset = true)
    }

    private fun fetchNextPage(reset: Boolean = false) {
        if (_state.value.isPaginating || _state.value.isLastPage) return

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            paginationMutex.withLock {
                _state.update { it.copy(isPaginating = true) }

                val s = _state.value
                val result = when {
                    !s.searchQuery.isNullOrBlank() -> {
                        productUseCases.filterProductsUseCase(
                            query = s.searchQuery,
                            category = s.selectedCategory,
                            page = currentPage,
                            size = pageSize
                        )
                    }
                    !s.selectedCategory.isNullOrBlank() ->
                        productUseCases.getProductsByCategoryUseCase(
                            s.selectedCategory,
                            currentPage,
                            pageSize
                        )
                    else ->
                        productUseCases.getProductsUseCase(currentPage, pageSize)
                }

                when (result) {
                    is ApiResult.Success -> {
                        val newProducts = result.data
                        currentPage++
                        _state.update {
                            it.copy(
                                products = if (reset) result.data else it.products + result.data,
                                isPaginating = false,
                                isRefreshing = false,
                                isInitialLoading = false,
                                isLastPage = newProducts.isEmpty()
                            )
                        }
                    }
                    is ApiResult.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message,
                                isPaginating = false,
                                isRefreshing = false,
                                isInitialLoading = false,
                                )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    // ======================== Helpers ========================
    fun clearMessage() {
        _state.update { it.copy(message = null, error = null) }
    }
}


data class ProductsViewState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val banner: List<Product> = emptyList(),

    val selectedCategory: String? = null,
    val searchQuery: String? = null,
    val isInitialLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaginating: Boolean = false,
    val isLastPage: Boolean = false,

    val error: String? = null,
    val message: String? = null
)