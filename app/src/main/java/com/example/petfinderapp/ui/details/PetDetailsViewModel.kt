package com.example.petfinderapp.ui.details

import com.example.petfinderapp.domain.models.PetDetails
import com.example.petfinderapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PetDetailsViewModel : BaseViewModel() {

    private val _petDetails = MutableStateFlow<PetDetails?>(null)
    val animalDetails = _petDetails.asStateFlow()

    fun onPetDetails(petDetails: PetDetails) {
        _petDetails.value = petDetails
    }
}
