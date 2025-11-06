package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.OrderResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.Product
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.AdminRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(private val repo: AdminRepository): ViewModel() {

    private val _usersState = MutableStateFlow<UiState<List<UserResponse>>>(UiState.Idle)
    val usersState = _usersState.asStateFlow()

    private val _deleteUserState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteUserState = _deleteUserState.asStateFlow()

    private val _editUserState = MutableStateFlow<UiState<UserResponse>>(UiState.Idle)
    val editUserState = _editUserState.asStateFlow()

    private val _ordersState = MutableStateFlow<UiState<List<OrderResponse>>>(UiState.Idle)
    val ordersState = _ordersState.asStateFlow()

    private val _orderStatusState = MutableStateFlow<UiState<OrderResponse>>(UiState.Idle)
    val orderStatusState = _orderStatusState.asStateFlow()

    private val _productsState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val productsState = _productsState.asStateFlow()

    private val _addProductState = MutableStateFlow<UiState<Product>>(UiState.Idle)
    val addProductState = _addProductState.asStateFlow()

    private val _deleteProductState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteProductState = _deleteProductState.asStateFlow()

    fun reset(){
        _usersState.update { UiState.Idle}
        _deleteUserState.update { UiState.Idle}
        _editUserState.update { UiState.Idle}
        _ordersState.update { UiState.Idle }
        _orderStatusState.update { UiState.Idle }
        _productsState.update { UiState.Idle }
        _deleteProductState.update { UiState.Idle }
    }

    fun resetAddProductState(){
        _addProductState.update { UiState.Idle }
    }

    fun refresh(){
        getAllUsers()
        getAllOrders()
        getAllProducts()
    }

    //============= Users ===========

    fun getAllUsers(){
        viewModelScope.launch {
            _usersState.update { UiState.Loading }
            val result = repo.getAllUsers()
            _usersState.update { result.toUiState()}
        }
    }

    fun deleteUser(userId: Long){
        viewModelScope.launch {
            _deleteUserState.update { UiState.Loading }
            val result = repo.deleteUser(userId)
            _deleteUserState.update { result.toUiState() }
        }
    }

    fun editUser(userId: Long, newUser: UserEditRequest){
        viewModelScope.launch {
            _editUserState.update { UiState.Loading }
            val result = repo.editUser(userId, newUser);
            _editUserState.update { result.toUiState() }
        }
    }

    //============= Products ===========

    fun getAllProducts(){
        viewModelScope.launch {
            _productsState.update { UiState.Loading }
            val result = repo.getAllProducts()
            _productsState.update { result.toUiState()}
        }
    }

    fun filterProducts(query: String){
        viewModelScope.launch {
            if(!query.isBlank()) {
                val result = repo.searchProduct(query)
                _productsState.update { result.toUiState() }
            }
        }
    }

    fun addProduct(product: Product){
        viewModelScope.launch {
            _addProductState.update { UiState.Loading }
            val result = repo.addProduct(product)
            _addProductState.update { result.toUiState() }
            if(addProductState.value is UiState.Success){
                getAllProducts()
            }
        }
    }

    fun deleteProduct(productId: Long){
        viewModelScope.launch {
            _deleteProductState.update { UiState.Loading }
            val result = repo.deleteProduct(productId)
            _deleteProductState.update { result.toUiState() }
            getAllProducts()
        }
    }

    fun updateProductStock(productId: Long, stock: Int){
        viewModelScope.launch {
            repo.updateProductStock(productId, stock)
        }
    }

    //============= Orders ===========
    fun getAllOrders(){
        viewModelScope.launch {
            _ordersState.update { UiState.Idle }
            val result = repo.getOrders()
            _ordersState.update { result.toUiState() }
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: String){
        viewModelScope.launch {
            _orderStatusState.update { UiState.Loading }
            val result = repo.updateOrderStatus(orderId, newStatus)
            _orderStatusState.update { result.toUiState() }
        }
    }
}