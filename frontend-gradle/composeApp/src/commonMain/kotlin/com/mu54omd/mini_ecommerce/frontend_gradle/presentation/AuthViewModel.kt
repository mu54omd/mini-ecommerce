package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.User
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.AuthUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases): ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthUiEffect>(
        replay = 1,
        extraBufferCapacity = 0
    )
    val effect = _effect.asSharedFlow()


    // ===========================================================================

    init {
        setUserInfo()
        validateStoredToken()
    }

    // ===========================================================================

    fun checkHealth(){
        viewModelScope.launch {
            _state.update { it.copy(isConnectionHealthy = false, isRefreshing = true) }
            when(val result = authUseCases.checkHealthUseCase()){
                is ApiResult.Success -> {
                    _state.update { it.copy(isConnectionHealthy = true, isRefreshing = false) }
                    _effect.emit(AuthUiEffect.NavigateToHomeAsGuest)
                }
                is ApiResult.NetworkError -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(AuthUiEffect.ShowError(result.exception.message))
                }
                else -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(AuthUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }

    fun login(username: String, password: String){
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            val result = authUseCases.loginUseCase(username, password)
            when(result){
                is ApiResult.Success -> {
                    _state.update { it.copy(isRefreshing = false, token = result.data) }
                    _effect.emit(AuthUiEffect.NavigateToHome)
                }
                is ApiResult.NetworkError -> {
                    _state.update { it.copy(isRefreshing = false, token = null) }
                    _effect.emit(AuthUiEffect.ShowError(result.exception.message))
                }
                else -> {
                    _state.update { it.copy(isRefreshing = false, token = null) }
                    _effect.emit(AuthUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
            setUserInfo()
        }
    }
    fun register(username: String, password: String, email: String){
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            val result = authUseCases.registerUseCase(username, password, email)
            when(result){
                is ApiResult.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(AuthUiEffect.ShowMessage(result.data.message))
                }
                is ApiResult.NetworkError -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(AuthUiEffect.ShowError(result.exception.message))
                }
                else -> {
                    _state.update { it.copy(isRefreshing = false) }
                    _effect.emit(AuthUiEffect.ShowError((result as ApiResult.Error).message))
                }
            }
        }
    }
    fun logout(){
        viewModelScope.launch {
            _state.update { AuthUiState() }
            authUseCases.logoutUseCase()
            _effect.emit(AuthUiEffect.LogOut)
        }
    }

    fun validateStoredToken(){
        viewModelScope.launch {
            authUseCases.validateTokenUseCase()?.let { token ->
                _state.update { it.copy(token = LoginResponse(response = token), isStoredTokenValid = true) }
                _effect.emit(AuthUiEffect.NavigateToHome)
            }
        }
    }

    fun clearToken(){
        viewModelScope.launch {
            authUseCases.clearTokenUseCase()
        }
    }

    private fun setUserInfo(){
        viewModelScope.launch {
            val user = authUseCases.getUserInfoUseCase()
           _state.update { it.copy(currentUser = user) }
        }
    }

}

data class AuthUiState(
    val token: LoginResponse? = null,
    val currentUser: User = User(),
    val isConnectionHealthy: Boolean = false,
    val isStoredTokenValid: Boolean = false,
    val isRefreshing: Boolean = false
)

sealed interface AuthUiEffect{
    data class ShowError(val message: String?): AuthUiEffect
    data class ShowMessage(val message: String?): AuthUiEffect
    object NavigateToHome: AuthUiEffect
    object NavigateToHomeAsGuest: AuthUiEffect
    object LogOut : AuthUiEffect

}