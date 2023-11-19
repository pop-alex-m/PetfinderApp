package com.example.petfinderapp.ui

import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.data.repositories.AuthorizationRepositoryImplementation
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val animalsRepository: AnimalsRepositoryImplementation,
    private val authorizationRepository: AuthorizationRepositoryImplementation
) : BaseViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _animalsList = MutableStateFlow(emptyList<AnimalDetails>())
    val animalsList: StateFlow<List<AnimalDetails>> = _animalsList

    fun checkTokenAndGetListOfAnimals(petType: SelectedPetType = SelectedPetType.DOG) {
        if (!authorizationRepository.isAccessTokenValid()) {
            val disposable = authorizationRepository.refreshAccessToken().subscribe({
                onRetrieveListOfPets(petType)
            }, { exception ->
                onError(exception)
            })
            compositeDisposable.add(disposable)
        } else {
            onRetrieveListOfPets(petType)
        }
    }

    private fun onRetrieveListOfPets(petType: SelectedPetType) {
        val disposable = animalsRepository.getAnimals(petType.name.lowercase(), 1)
            .subscribe({ animalDetailsList ->
                _animalsList.value = animalDetailsList
            }, { exception ->
                onError(exception)
            })
        compositeDisposable.add(disposable)
    }
}