package com.example.petfinderapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderapp.domain.models.AuthorizationException
import com.example.petfinderapp.domain.models.GenericNetworkException
import com.example.petfinderapp.domain.models.InternalServerError
import com.example.petfinderapp.domain.models.NoConnectivityException
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage

    protected fun onError(error: Throwable) {
        viewModelScope.launch {
            _errorMessage.emit(getErrorMessage(error))
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    private fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is NoConnectivityException -> "Could not connect to the internet, please try again later"
            is AuthorizationException -> "Authentication failed"
            is InternalServerError -> "We are experiencing an server issue"
            is GenericNetworkException -> "Oops, something went wrong"
            else -> "Oops, something went wrong"
        }
    }
}