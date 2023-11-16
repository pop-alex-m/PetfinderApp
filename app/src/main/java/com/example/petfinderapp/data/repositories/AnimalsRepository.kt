package com.example.petfinderapp.data.repositories

import com.example.petfinderapp.data.models.Breed
import com.example.petfinderapp.data.models.PetResponse
import com.example.petfinderapp.data.network.PetFinderApiService
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.domain.models.AuthorizationException
import com.example.petfinderapp.domain.models.GenericNetworkException
import com.example.petfinderapp.domain.models.InternalServerError
import com.example.petfinderapp.domain.models.NoConnectivityException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import java.net.UnknownHostException

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
            return@onErrorResumeNext when (it) {
                is UnknownHostException -> {
                    // No connectivity error
                    Single.error(NoConnectivityException())
                }

                is HttpException -> {
                    when {
                        it.code() == 401 -> {
                            // Unauthorized
                            Single.error(AuthorizationException())
                        }

                        it.code() == 500 || it.code() == 501 -> {
                            // Internal server error
                            Single.error(InternalServerError())
                        }

                        else -> {
                            // Other server error codes
                            Single.error(GenericNetworkException())
                        }
                    }
                }

                else -> {
                    Single.error(it)
                }
            }
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
