package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun reset(){
        _usersState.update { UiState.Idle}
        _deleteUserState.update { UiState.Idle}
        _editUserState.update { UiState.Idle}
    }

    fun refresh(){
        getAllUsers()
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

    fun addProduct(product: Product){
        viewModelScope.launch {
            repo.addProduct(product)
        }
    }

    fun deleteProduct(productId: Long){
        viewModelScope.launch {
            repo.deleteProduct(productId)
        }
    }

    fun updateProductStock(productId: Long, stock: Int){
        viewModelScope.launch {
            repo.updateProductStock(productId, stock)
        }
    }

    //============= Orders ===========

}