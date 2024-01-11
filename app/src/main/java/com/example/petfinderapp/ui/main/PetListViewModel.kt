package com.example.petfinderapp.ui.main

import androidx.paging.PagingData
import com.example.petfinderapp.data.repositories.AnimalsRepository
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.ui.SelectedPetType
import com.example.petfinderapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class PetListViewModel(private val animalsRepository: AnimalsRepository) : BaseViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getListOfAnimals(petType: SelectedPetType): Flow<PagingData<AnimalDetails>> {
        return animalsRepository.getAnimalsByPage(petType)
    }
}