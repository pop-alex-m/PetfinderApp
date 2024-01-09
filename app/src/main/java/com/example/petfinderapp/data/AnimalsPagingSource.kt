package com.example.petfinderapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.petfinderapp.data.models.Breed
import com.example.petfinderapp.data.models.PetResponse
import com.example.petfinderapp.data.network.PetFinderApiService
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.ui.SelectedPetType
import java.util.Locale

class AnimalsPagingSource(
    private val petFinderApiService: PetFinderApiService,
    private val petType: SelectedPetType
) : PagingSource<Int, AnimalDetails>() {

    companion object {
        private const val UNKNOWN_BREED = "Unknown"
        private const val BREED_PRIMARY = "Primary breed : "
        private const val BREED_SECONDARY = "Secondary breed : "
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimalDetails> {
        val position = params.key ?: 1
        return try {
            val responseObservable = petFinderApiService.getListOfAnimals(
                petType.name.lowercase(Locale.getDefault()),
                position
            ).blockingGet()
            LoadResult.Page(
                data = mapResponse(responseObservable),
                prevKey = if (position == 1) null else (position - 1),
                nextKey = (position + 1)
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    private fun mapResponse(petResponse: PetResponse): List<AnimalDetails> {
        return petResponse.animals.map {
            AnimalDetails(
                name = it.name,
                gender = it.gender,
                size = it.size,
                breed = getBreedAsText(it.breed),
                status = it.status,
                distance = it.distance
            )
        }
    }

    private fun getBreedAsText(breed: Breed?): String {
        return breed?.let {
            when {
                breed.unknown == true -> {
                    UNKNOWN_BREED
                }

                breed.mixed == true -> {
                    BREED_PRIMARY + breed.primary + BREED_SECONDARY + breed.secondary
                }

                else -> {
                    breed.primary ?: UNKNOWN_BREED
                }
            }
        } ?: UNKNOWN_BREED
    }

    override fun getRefreshKey(state: PagingState<Int, AnimalDetails>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}