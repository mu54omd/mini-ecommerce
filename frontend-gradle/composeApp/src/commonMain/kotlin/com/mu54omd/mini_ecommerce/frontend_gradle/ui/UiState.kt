package com.mu54omd.mini_ecommerce.frontend_gradle.ui

import com.mu54omd.mini_ecommerce.frontend_gradle.api.ApiResult


sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(
        val message: String,
        val fields: Map<String, String>? = null
    ) : UiState<Nothing>()
}
fun <T> ApiResult<T>.toUiState(): UiState<T> = when (this) {
    is ApiResult.Success -> UiState.Success(data)
    is ApiResult.Unauthorized -> UiState.Error(message = message ?: "Unauthorized Action: ${code ?: ""}")
    is ApiResult.Forbidden -> UiState.Error(message = message ?: "Forbidden Action: ${code ?: ""}")
    is ApiResult.Error -> UiState.Error(message = message ?: "Error ${code ?: ""}")
    is ApiResult.NetworkError -> UiState.Error(message = "Network error: ${exception.message}")
    is ApiResult.ValidationError -> UiState.Error(
        message = message ?: "Validation failed",
        fields = fields
    )
}