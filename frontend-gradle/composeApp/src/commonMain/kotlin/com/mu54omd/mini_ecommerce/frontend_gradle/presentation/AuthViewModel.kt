package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.LoginResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.RegisterResponse
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.model.User
import com.mu54omd.mini_ecommerce.frontend_gradle.domain.usecase.AuthUseCases
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases): ViewModel() {

    private val _tokenState: MutableStateFlow<UiState<LoginResponse>> = MutableStateFlow(UiState.Idle)
    val tokenState: StateFlow<UiState<LoginResponse>> = _tokenState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.Idle
        )

    private val _userState: MutableStateFlow<User> = MutableStateFlow(User())
    val userState: StateFlow<User> = _userState.asStateFlow()

    private val _registerState: MutableStateFlow<UiState<RegisterResponse>> = MutableStateFlow(UiState.Idle)
    val registerState = _registerState.asStateFlow()

    // ===========================================================================

    init {
        validateStoredToken()
        setUserInfo()
    }

    // ===========================================================================

    fun resetAllStates(){
        resetTokenState()
        resetUserState()
        resetRegisterState()
    }

    fun resetTokenState(){
        _tokenState.update { UiState.Idle }
    }

    fun resetUserState(){
        _userState.update { User() }
    }

    fun resetRegisterState(){
        _registerState.update { UiState.Idle }
    }

    // ===========================================================================

    fun login(username: String, password: String){
        viewModelScope.launch {
            _tokenState.update { UiState.Loading }
            val result = authUseCases.loginUseCase(username, password)
            _tokenState.update { result.toUiState() }
            setUserInfo()
        }
    }
    fun register(username: String, password: String, email: String){
        viewModelScope.launch {
            _registerState.update { UiState.Loading }
            val result = authUseCases.registerUseCase(username, password, email)
            _registerState.update { result.toUiState() }
        }
    }
    fun logout(cause: UiState<*> = UiState.Idle){
        if(cause is UiState.Error || cause is UiState.Unauthorized || cause is UiState.Idle || cause is UiState.Loading) {
            _tokenState.update { cause as UiState<LoginResponse> }
            viewModelScope.launch {
                authUseCases.logoutUseCase()
                setUserInfo()
            }
        }
    }

    fun validateStoredToken(){
        viewModelScope.launch {
            authUseCases.validateTokenUseCase()?.let { token -> _tokenState.update { UiState.Success(LoginResponse(response = token)) } }
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
           _userState.update { user }
        }
    }

}