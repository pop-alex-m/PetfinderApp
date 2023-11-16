package com.example.petfinderapp.ui

import android.util.Log
import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.data.repositories.AuthorizationRepositoryImplementation
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : BaseViewModel(), KoinComponent {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val animalsRepository: AnimalsRepositoryImplementation by inject()
    private val authorizationRepository: AuthorizationRepositoryImplementation by inject()

    private val _animalsList = MutableStateFlow(emptyList<AnimalDetails>())
    val animalsList: StateFlow<List<AnimalDetails>> = _animalsList

    fun refreshAuthorization(){
        val disposable = authorizationRepository.refreshAccessToken().subscribe ({
            onRetrieveListOfPets()
        } , { exception ->
            Log.e(TAG, "Error on authorization " + exception.message)
        } )
        compositeDisposable.add(disposable)
    }

    fun onRetrieveListOfPets(petType: SelectedPetType = SelectedPetType.DOG): String? {
        val page = 1
        val disposable = animalsRepository.getAnimals(petType.name.lowercase(), page)
            .subscribe({ animalDetailsList ->
                _animalsList.value = animalDetailsList
            }, { exception ->
                onError(exception)
            })
        compositeDisposable.add(disposable)
        return null
    }
}