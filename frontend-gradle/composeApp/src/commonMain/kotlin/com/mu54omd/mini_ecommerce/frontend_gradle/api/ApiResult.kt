package com.mu54omd.mini_ecommerce.frontend_gradle.api

sealed class ApiResult<out T> {

    data class Success<out T>(val data: T) : ApiResult<T>()

    data class ValidationError(
        val message: String? = null,
        val fields: Map<String, String>? = null,
        val code: Int? = 400
    ) : ApiResult<Nothing>()

    data class Unauthorized(
        val message: String? = null,
        val code: Int? = 401
    ) : ApiResult<Nothing>()

    data class Forbidden(
        val message: String? = null,
        val code: Int? = 403
    ) : ApiResult<Nothing>()

    data class Error(
        val message: String? = null,
        val code: Int? = null
    ) : ApiResult<Nothing>()

    data class NetworkError(val exception: Throwable) : ApiResult<Nothing>()
}

suspend fun <T, R> ApiResult<T>.map(
    onSuccess: (T) -> R,
    onAfterSuccess: (suspend (R) -> Unit)? = null
): ApiResult<R> = when (this) {
    is ApiResult.Success -> {
        val newData = onSuccess(data)
        onAfterSuccess?.invoke(newData)
        ApiResult.Success(newData)
    }
    is ApiResult.Error -> ApiResult.Error(message, code)
    is ApiResult.Unauthorized -> ApiResult.Unauthorized(message)
    is ApiResult.Forbidden -> ApiResult.Forbidden(message)
    is ApiResult.NetworkError -> ApiResult.Error(message = this.exception.message)
    is ApiResult.ValidationError -> ApiResult.ValidationError(message, fields, code)
}

