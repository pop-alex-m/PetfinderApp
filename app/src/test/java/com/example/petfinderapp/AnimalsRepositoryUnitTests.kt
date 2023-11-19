package com.example.petfinderapp

import com.example.petfinderapp.data.models.Animal
import com.example.petfinderapp.data.models.Breed
import com.example.petfinderapp.data.models.PetResponse
import com.example.petfinderapp.data.network.PetFinderApiService
import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.domain.models.AuthorizationException
import com.example.petfinderapp.domain.models.GenericNetworkException
import com.example.petfinderapp.domain.models.InternalServerError
import com.example.petfinderapp.domain.models.NoConnectivityException
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException


class AnimalsRepositoryUnitTests : KoinTest {

    private val animalsRepository by inject<AnimalsRepositoryImplementation>()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @get:Rule
    val mainDispatcherRule = TestRxSchedulerRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { Mockito.mock(PetFinderApiService::class.java) }
                single { AnimalsRepositoryImplementation(get()) }
            }
        )
    }

    @Test
    fun getAnimals_success() {
        val animalsList = mutableListOf<Animal>()
        val breed = Breed(
            primary = "chihuahua",
            secondary = null,
            mixed = false,
            unknown = false
        )
        animalsList.add(
            Animal(
                name = "Doggo1",
                gender = "male",
                size = "small",
                breed = breed,
                status = "available",
                distance = "245km"
            )
        )
        val animalsResponse = PetResponse(animalsList)

        declareMock<PetFinderApiService> {
            BDDMockito.given(getListOfAnimals("dog", 1)).willReturn(Single.just(animalsResponse))
        }

        val testObserver = TestObserver<List<AnimalDetails>>()
        val result = animalsRepository.getAnimals("dog", 1)
        result.subscribe(testObserver)

        val animalsDetailsList = mutableListOf<AnimalDetails>()
        animalsDetailsList.add(
            AnimalDetails(
                name = "Doggo1",
                gender = "male",
                size = "small",
                breed = "chihuahua",
                status = "available",
                distance = "245km"
            )
        )
        testObserver.assertValue(animalsDetailsList)
    }

    @Test
    fun getAnimals_error_500_internal_server() {
        val httpInternalServerErrorException = HttpException(
            Response.error<ResponseBody>(
                500,
                "".toResponseBody("plain/text".toMediaTypeOrNull())
            )
        )

        declareMock<PetFinderApiService> {
            BDDMockito.given(getListOfAnimals("dog", 1))
                .willReturn(Single.error(httpInternalServerErrorException))
        }

        val testObserver = TestObserver<List<AnimalDetails>>()
        val result = animalsRepository.getAnimals("dog", 1)
        result.subscribe(testObserver)
        testObserver.assertError(InternalServerError::class.java)
    }

    @Test
    fun getAnimals_error_401_authorization() {
        val httpAuthorizationErrorException = HttpException(
            Response.error<ResponseBody>(
                401,
                "".toResponseBody("plain/text".toMediaTypeOrNull())
            )
        )

        declareMock<PetFinderApiService> {
            BDDMockito.given(getListOfAnimals("dog", 1))
                .willReturn(Single.error(httpAuthorizationErrorException))
        }

        val testObserver = TestObserver<List<AnimalDetails>>()
        val result = animalsRepository.getAnimals("dog", 1)
        result.subscribe(testObserver)
        testObserver.assertError(AuthorizationException::class.java)
    }

    @Test
    fun getAnimals_error_402_generic_error() {
        val httpGenericError = HttpException(
            Response.error<ResponseBody>(
                402,
                "".toResponseBody("plain/text".toMediaTypeOrNull())
            )
        )

        declareMock<PetFinderApiService> {
            BDDMockito.given(getListOfAnimals("dog", 1)).willReturn(Single.error(httpGenericError))
        }

        val testObserver = TestObserver<List<AnimalDetails>>()
        val result = animalsRepository.getAnimals("dog", 1)
        result.subscribe(testObserver)
        testObserver.assertError(GenericNetworkException::class.java)
    }

    @Test
    fun getAnimals_error_noConnectivity() {
        declareMock<PetFinderApiService> {
            BDDMockito.given(getListOfAnimals("dog", 1))
                .willReturn(Single.error(UnknownHostException()))
        }

        val testObserver = TestObserver<List<AnimalDetails>>()
        val result = animalsRepository.getAnimals("dog", 1)
        result.subscribe(testObserver)
        testObserver.assertError(NoConnectivityException::class.java)
    }
}