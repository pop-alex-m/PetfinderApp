package com.example.petfinderapp.data

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class NetworkError(val exception: Exception) : Result<Nothing>()
    data class AuthorizationNotFoundError(val exception: Exception) : Result<Nothing>()
    data class NoConnectionError(val exception: Exception) : Result<Nothing>()
}