package com.example.petfinderapp.data.network

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        ResultWrapper<Nothing>()

    data object NoConnectionError : ResultWrapper<Nothing>()
    data object AuthorizationNotFoundError : ResultWrapper<Nothing>()
    data object InternalServerError : ResultWrapper<Nothing>()
    data object RateLimitExceeded : ResultWrapper<Nothing>()
}
