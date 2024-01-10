package com.example.petfinderapp.ui

import androidx.paging.PagingData
import com.example.petfinderapp.data.repositories.AnimalsRepository
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val animalsRepository: AnimalsRepository) : BaseViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _animalsList = MutableStateFlow(PagingData.empty<AnimalDetails>())
    val animalsList: StateFlow<PagingData<AnimalDetails>> = _animalsList

    fun getListOfAnimals(petType: SelectedPetType): Flow<PagingData<AnimalDetails>> {
        return animalsRepository.getAnimalsByPage(petType)
    }

}