package com.example.petfinderapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.petfinderapp.data.models.Animal
import com.example.petfinderapp.data.models.Breed
import com.example.petfinderapp.data.models.PetResponse
import com.example.petfinderapp.data.network.NetworkUtils.Companion.safeApiCall
import com.example.petfinderapp.data.network.ResultWrapper
import com.example.petfinderapp.data.network.services.PetFinderApiService
import com.example.petfinderapp.domain.models.ApiRateLimitExceededException
import com.example.petfinderapp.domain.models.AuthorizationException
import com.example.petfinderapp.domain.models.GenericNetworkException
import com.example.petfinderapp.domain.models.InternalServerErrorException
import com.example.petfinderapp.domain.models.NoConnectivityException
import com.example.petfinderapp.domain.models.PetDetails
import com.example.petfinderapp.domain.models.SelectedPetType
import kotlinx.coroutines.Dispatchers
import java.util.Locale

class AnimalsPagingSource(
    private val petFinderApiService: PetFinderApiService,
    private val petType: SelectedPetType
) : PagingSource<Int, PetDetails>() {

    companion object {
        private const val UNKNOWN_BREED = "Unknown"
        private const val BREED_PRIMARY = "Primary breed : "
        private const val BREED_SECONDARY = "Secondary breed : "
        private const val apiPageLimit = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PetDetails> {
        val page = params.key ?: 1
        return try {
            val petResponse = safeApiCall(Dispatchers.IO) {
                petFinderApiService.getListOfAnimals(
                    petType.name.lowercase(Locale.getDefault()), page, apiPageLimit
                )
            }
            when (petResponse) {
                is ResultWrapper.Success -> {
                    LoadResult.Page(
                        data = mapResponse(petResponse.value),
                        prevKey = if (page == 1) null else (page - 1),
                        nextKey = (page + 1)
                    )
                }

                is ResultWrapper.AuthorizationNotFoundError -> throw AuthorizationException()
                is ResultWrapper.GenericError -> throw GenericNetworkException()
                is ResultWrapper.InternalServerError -> throw InternalServerErrorException()
                is ResultWrapper.NoConnectionError -> throw NoConnectivityException()
                is ResultWrapper.RateLimitExceeded -> throw ApiRateLimitExceededException()
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    private fun mapResponse(petResponse: PetResponse): List<PetDetails> {
        return petResponse.animals.map {
            PetDetails(
                id = it.id,
                name = it.name,
                gender = it.gender,
                size = it.size,
                breed = getBreedAsText(it.breed),
                status = it.status,
                distance = it.distance,
                smallPhotoUrl = getPhotoUrl(it, 0),
                largePhotoUrl = getPhotoUrl(it, 1)
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

    private fun getPhotoUrl(animal: Animal, size: Int = 0): String? {
        val photos = animal.photos
        return if (!photos.isNullOrEmpty()) {
            if (size == 0) photos[0].small else photos[0].large
        } else null
    }

    override fun getRefreshKey(state: PagingState<Int, PetDetails>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}