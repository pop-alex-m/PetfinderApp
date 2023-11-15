package com.example.petfinderapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderapp.domain.models.NetworkError
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

open class BaseViewModel : ViewModel(), KoinComponent {

    val compositeDisposable = CompositeDisposable()

    private val _errorMessage = MutableSharedFlow<NetworkError>()
    val errorMessage: SharedFlow<NetworkError?> = _errorMessage

    protected fun onNetworkError(networkError: NetworkError) {
        viewModelScope.launch {
            _errorMessage.emit(networkError)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}