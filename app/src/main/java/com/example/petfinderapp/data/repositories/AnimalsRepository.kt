package com.example.petfinderapp.data.repositories

import com.example.petfinderapp.data.PetFinderApiService
import com.example.petfinderapp.data.models.Breed
import com.example.petfinderapp.data.models.PetResponse
import com.example.petfinderapp.domain.models.AnimalDetails
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AnimalsRepository {

    fun getAnimals(type: String, page: Int): Single<List<AnimalDetails>>
}

class AnimalsRepositoryImplementation : AnimalsRepository, KoinComponent {

    private val apiService: PetFinderApiService by inject()

    companion object {
        private const val TAG = "AnimalsRepositoryImplementation"
        private const val UNKNOWN_BREED = "Unknown"
        private const val BREED_PRIMARY = "Primary breed : "
        private const val BREED_SECONDARY = "Secondary breed : "
    }

    override fun getAnimals(type: String, page: Int): Single<List<AnimalDetails>> {
        return apiService.getListOfAnimals(type, page).flatMap { petResponse ->
            val animalDetailsList = mapResponse(petResponse)
            return@flatMap Single.just(animalDetailsList)
        }.onErrorResumeNext {
            return@onErrorResumeNext Single.error(it)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
}
