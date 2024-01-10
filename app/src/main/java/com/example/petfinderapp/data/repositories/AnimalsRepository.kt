package com.example.petfinderapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.petfinderapp.data.AnimalsPagingSource
import com.example.petfinderapp.data.network.services.PetFinderApiService
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.ui.SelectedPetType
import kotlinx.coroutines.flow.Flow


interface AnimalsRepository {
    fun getAnimalsByPage(petType: SelectedPetType): Flow<PagingData<AnimalDetails>>
}

class AnimalsRepositoryImplementation(private val apiService: PetFinderApiService) :
    AnimalsRepository {

    override fun getAnimalsByPage(petType: SelectedPetType): Flow<PagingData<AnimalDetails>> {
        return Pager(
            config = PagingConfig(pageSize = 20, maxSize = 200),
            pagingSourceFactory = { AnimalsPagingSource(petFinderApiService = apiService, petType) }
        ).flow
    }
}
