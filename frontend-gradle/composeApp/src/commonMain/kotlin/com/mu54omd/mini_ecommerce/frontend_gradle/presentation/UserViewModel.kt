package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserEditRequest
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.UserResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.UserUseCases
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.create_user_successful_alert
import frontend_gradle.composeapp.generated.resources.delete_user_successful_alert
import frontend_gradle.composeapp.generated.resources.update_user_successful_alert
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class UserViewModel(
    private val userUseCases: UserUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(UserUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<UserUiEffect>()
    val effect = _effect.asSharedFlow()


    //============================================================

    fun getAllUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            when(val result = userUseCases.getAllUsersUseCase()){
                is ApiResult.Success -> {
                    _state.update { it.copy(users = result.data, isRefreshing = false) }
                }
                else -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(UserUiEffect.ShowError((result as ApiResult.Error).message))

                }
            }
        }
    }

    fun createUser(newUser: UserEditRequest){
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            when(val result = userUseCases.createUserUseCase(newUser)){
                is ApiResult.Success -> {
                    getAllUsers()
                    _effect.emit(UserUiEffect.ShowMessage(getString(Res.string.create_user_successful_alert)))
                }
                else -> {
                    _effect.emit(UserUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            when(val result = userUseCases.deleteUserUseCase(userId)){
                is ApiResult.Success -> {
                    getAllUsers()
                    _effect.emit(UserUiEffect.ShowMessage(getString(Res.string.delete_user_successful_alert)))
                }
                else -> {
                    _effect.emit(UserUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }

    fun editUser(userId: Long, userRequest: UserEditRequest) {
        _state.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            when(val result = userUseCases.editUserUseCase(userId, userRequest)){
                is ApiResult.Success -> {
                    getAllUsers()
                    _effect.emit(UserUiEffect.ShowMessage(getString(Res.string.update_user_successful_alert)))
                }
                else -> {
                    _effect.emit(UserUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }

}

data class UserUiState(
    val users: List<UserResponse> = emptyList(),
    val isRefreshing: Boolean = false,
)

sealed interface UserUiEffect {
    data class ShowMessage(val text: String?) : UserUiEffect
    data class ShowError(val text: String?) : UserUiEffect
}