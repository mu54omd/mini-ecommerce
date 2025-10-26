package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository): ViewModel() {

    private val _loginState: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.LoggedOut)
    val loginState: StateFlow<UiState<String>> =
        _loginState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.LoggedOut
        )

    init {
        validateStoredToken()
    }
    fun login(username: String, password: String){
        viewModelScope.launch {
            _loginState.update { UiState.Loading }
            val result = repo.login(username, password)
            _loginState.update { result.toUiState() }
        }
    }
    fun logout(){
        viewModelScope.launch {
            _loginState.update { UiState.LoggedOut }
            repo.logout()
        }
    }

    fun validateStoredToken(){
        viewModelScope.launch {
            repo.validToken()?.let { token -> _loginState.update { UiState.Success(token) } }
        }
    }
}