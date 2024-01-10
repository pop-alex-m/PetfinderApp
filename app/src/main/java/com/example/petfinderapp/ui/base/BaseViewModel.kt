package com.example.petfinderapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderapp.domain.models.ApiRateLimitExceededException
import com.example.petfinderapp.domain.models.AuthorizationException
import com.example.petfinderapp.domain.models.GenericNetworkException
import com.example.petfinderapp.domain.models.InternalServerErrorException
import com.example.petfinderapp.domain.models.NoConnectivityException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage

    fun onError(error: Throwable) {
        viewModelScope.launch {
            _errorMessage.emit(getErrorMessage(error))
        }
    }

    private fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is NoConnectivityException -> "Could not connect to the internet, please try again later"
            is AuthorizationException -> "Authentication failed"
            is InternalServerErrorException -> "We are experiencing an server issue"
            is GenericNetworkException -> "Oops, something went wrong"
            is ApiRateLimitExceededException -> "Pathfinder API Rate Limit Exceeded"
            else -> "Oops, something went wrong"
        }
    }
}