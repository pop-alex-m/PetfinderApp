package com.example.petfinderapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.petfinderapp.data.AnimalsPagingSource
import com.example.petfinderapp.data.network.services.PetFinderApiService
import com.example.petfinderapp.domain.models.PetDetails
import com.example.petfinderapp.domain.models.SelectedPetType
import kotlinx.coroutines.flow.Flow


interface AnimalsRepository {
    fun getAnimalsByPage(petType: SelectedPetType): Flow<PagingData<PetDetails>>
}

class AnimalsRepositoryImplementation(private val apiService: PetFinderApiService) :
    AnimalsRepository {

    override fun getAnimalsByPage(petType: SelectedPetType): Flow<PagingData<PetDetails>> {
        return Pager(
            config = PagingConfig(pageSize = 20, maxSize = 200),
            pagingSourceFactory = { AnimalsPagingSource(petFinderApiService = apiService, petType) }
        ).flow
    }
}
