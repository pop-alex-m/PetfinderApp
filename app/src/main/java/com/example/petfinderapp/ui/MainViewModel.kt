package com.example.petfinderapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.data.repositories.AuthorizationRepositoryImplementation
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.domain.models.NetworkError
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val animalsRepository : AnimalsRepositoryImplementation by inject()
    private val authorizationRepository : AuthorizationRepositoryImplementation by inject()

    private val compositeDisposable = CompositeDisposable()

    private val _animalsList = MutableStateFlow(emptyList<AnimalDetails>())
    val animalsList: StateFlow<List<AnimalDetails>> = _animalsList

    private val _networkError = MutableSharedFlow<NetworkError>()
    val networkError: SharedFlow<NetworkError?> = _networkError

    fun refreshAuthorization(){
        val disposable = authorizationRepository.refreshAccessToken().subscribe ({
             getListOfPets()
        } , { exception ->
            Log.e(TAG, "Error on authorization " + exception.message)
        } )
        compositeDisposable.add(disposable)
    }

    fun getListOfPets(): String? {
        val type = "dog"
        val page = 1
        val disposable = animalsRepository.getAnimals(type, page).subscribe({ animalDetailsList ->
            _animalsList.value = animalDetailsList
        }, { exception ->
            Log.e(TAG, "Error " + exception.message)
            viewModelScope.launch {
                _networkError.emit(NetworkError.AUTHENTICATION_ERROR)
            }
        })
        compositeDisposable.add(disposable)
        return null
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}