package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.data.models.User
import com.mu54omd.mini_ecommerce.frontend_gradle.data.repository.AuthRepository
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.UiState
import com.mu54omd.mini_ecommerce.frontend_gradle.ui.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository): ViewModel() {

    private val _tokenState: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Idle)
    val tokenState: StateFlow<UiState<String>> = _tokenState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.Idle
        )

    private val _userState: MutableStateFlow<User> = MutableStateFlow(User())
    val userState: StateFlow<User> = _userState.asStateFlow()

    init {
        validateStoredToken()
        setUserInfo()
    }
    fun reset(){
        _tokenState.update { UiState.Idle }
        _userState.update { User() }
    }
    fun login(username: String, password: String){
        viewModelScope.launch {
            _tokenState.update { UiState.Loading }
            val result = repo.login(username, password)
            _tokenState.update { result.toUiState() }
            setUserInfo()
        }
    }
    fun logout(cause: UiState<*> = UiState.Idle){
        _tokenState.update { cause as UiState<String> }
        viewModelScope.launch {
            repo.logout()
            setUserInfo()
        }
    }

    fun validateStoredToken(){
        viewModelScope.launch {
            repo.validToken()?.let { token -> _tokenState.update { UiState.Success(token) } }
        }
    }

    private fun setUserInfo(){
        viewModelScope.launch {
            val user = repo.getUserInfo()
           _userState.update { user }
            println(userState.value)
        }
    }
}