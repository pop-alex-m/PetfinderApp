package com.example.petfinderapp

import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.data.repositories.AuthorizationRepositoryImplementation
import com.example.petfinderapp.domain.models.AnimalDetails
import com.example.petfinderapp.domain.models.AuthorizationException
import com.example.petfinderapp.domain.models.GenericNetworkException
import com.example.petfinderapp.domain.models.InternalServerError
import com.example.petfinderapp.domain.models.NoConnectivityException
import com.example.petfinderapp.ui.MainViewModel
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import kotlin.test.assertEquals

class MainViewModelUnitTest : KoinTest {

    private val mainViewModel by inject<MainViewModel>()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { AnimalsRepositoryImplementation() }
                single { AuthorizationRepositoryImplementation() }
                viewModel { MainViewModel() }
            }
        )
    }

    @Test
    fun testGetAnimalsResponse_success() {
        val animalsResponse = mutableListOf<AnimalDetails>()
        animalsResponse.add(
            AnimalDetails(
                name = "Doggo1",
                gender = "male",
                size = "small",
                breed = "chihuahua",
                status = "available",
                distance = "245km"
            )
        )
        animalsResponse.add(
            AnimalDetails(
                name = "Doggo2",
                gender = "female",
                size = "medium",
                breed = "pitbull",
                status = "available",
                distance = "40km"
            )
        )

        declareMock<AnimalsRepositoryImplementation> {
            given(this.getAnimals("dog", 1)).willReturn(Single.just(animalsResponse))
        }

        with(mainViewModel) {
            onRetrieveListOfPets()
            assertEquals(animalsList.value.size, 2)
        }

        with(mainViewModel.animalsList.value[0]) {
            assertEquals(name, "Doggo1")
            assertEquals(gender, "male")
            assertEquals(size, "small")
            assertEquals(breed, "chihuahua")
            assertEquals(status, "available")
            assertEquals(distance, "245km")
        }
    }

    @Test
    fun testGetAnimalsResponse_error_no_connectivity() {
        declareMock<AnimalsRepositoryImplementation> {
            given(this.getAnimals("dog", 1)).willReturn(Single.error(NoConnectivityException()))
        }
        with(mainViewModel) {
            onRetrieveListOfPets()
            assertEquals(animalsList.value.size, 0)
            assertEquals(
                errorMessage.value,
                "Could not connect to the internet, please try again later"
            )
        }
    }

    @Test
    fun testGetAnimalsResponse_error_authorization() {
        declareMock<AnimalsRepositoryImplementation> {
            given(this.getAnimals("dog", 1)).willReturn(Single.error(AuthorizationException()))
        }
        with(mainViewModel) {
            onRetrieveListOfPets()
            assertEquals(animalsList.value.size, 0)
            assertEquals(errorMessage.value, "Authentication failed")
        }
    }

    @Test
    fun testGetAnimalsResponse_error_generic_network_error() {
        declareMock<AnimalsRepositoryImplementation> {
            given(this.getAnimals("dog", 1)).willReturn(Single.error(GenericNetworkException()))
        }
        with(mainViewModel) {
            onRetrieveListOfPets()
            assertEquals(animalsList.value.size, 0)
            assertEquals(errorMessage.value, "Oops, something went wrong")
        }
    }

    @Test
    fun testGetAnimalsResponse_error_internal_server_error() {
        declareMock<AnimalsRepositoryImplementation> {
            given(this.getAnimals("dog", 1)).willReturn(Single.error(InternalServerError()))
        }
        with(mainViewModel) {
            onRetrieveListOfPets()
            assertEquals(animalsList.value.size, 0)
            assertEquals(errorMessage.value, "We are experiencing an server issue")
        }
    }
}