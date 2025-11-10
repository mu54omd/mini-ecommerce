package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.UserUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val userUseCases: UserUseCases,
) : ViewModel() {

    private val _usersState = MutableStateFlow<UiState<List<UserResponse>>>(UiState.Idle)
    val usersState = _usersState.asStateFlow()

    private val _deleteUserState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteUserState = _deleteUserState.asStateFlow()

    private val _editUserState = MutableStateFlow<UiState<UserResponse>>(UiState.Idle)
    val editUserState = _editUserState.asStateFlow()

    //============================================================


    fun resetAllStates() {
        resetUsersState()
        resetDeleteUserState()
        resetEditUserState()
    }

    fun resetUsersState(){
        _usersState.update { UiState.Idle }
    }

    fun resetDeleteUserState(){
        _deleteUserState.update { UiState.Idle }
    }

    fun resetEditUserState(){
        _editUserState.update { UiState.Idle }
    }

    //============================================================

    fun getAllUsers() {
        viewModelScope.launch {
            _usersState.update { UiState.Loading }
            val result = userUseCases.getAllUsersUseCase()
            _usersState.update { result.toUiState() }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            _deleteUserState.update { UiState.Loading }
            val result = userUseCases.deleteUserUseCase(userId)
            _deleteUserState.update { result.toUiState() }
            println(deleteUserState.value)
        }
    }

    fun editUser(userId: Long, newUser: UserEditRequest) {
        viewModelScope.launch {
            _editUserState.update { UiState.Loading }
            val result = userUseCases.editUserUseCase(userId, newUser);
            _editUserState.update { result.toUiState() }
        }
    }

}