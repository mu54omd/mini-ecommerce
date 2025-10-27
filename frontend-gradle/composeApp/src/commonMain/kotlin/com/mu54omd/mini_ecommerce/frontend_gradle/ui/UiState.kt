package com.mu54omd.mini_ecommerce.frontend_gradle.ui

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult

sealed class UiState<out T> {
    object Idle: UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Unauthorized : UiState<Nothing>()
}

fun <T> ApiResult<T>.toUiState(): UiState<T> = when (this) {
    is ApiResult.Success -> UiState.Success(data)
    is ApiResult.Unauthorized -> UiState.Unauthorized
    is ApiResult.Forbidden -> UiState.Error(message ?: "Forbidden")
    is ApiResult.Error -> UiState.Error(message ?: "Error $code")
    is ApiResult.NetworkError -> UiState.Error("Network error: ${exception.message}")
}