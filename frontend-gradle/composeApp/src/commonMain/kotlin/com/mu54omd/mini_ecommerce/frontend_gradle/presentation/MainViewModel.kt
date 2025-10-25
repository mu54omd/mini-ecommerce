package com.mu54omd.mini_ecommerce.frontend_gradle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiClient
import com.mu54omd.mini_ecommerce.frontend_gradle.storage.getSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(apiClient: ApiClient): ViewModel() {
    private var _loginState: MutableStateFlow<String?> = MutableStateFlow("")
    val loginState: StateFlow<String?>
        get() = _loginState

    init {
        getToken()
    }

    private fun getToken(){
        viewModelScope.launch {
            _loginState.update { getSessionManager().getToken()}
        }
    }
}