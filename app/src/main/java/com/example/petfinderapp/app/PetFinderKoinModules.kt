package com.example.petfinderapp.app

import com.example.petfinderapp.data.TokenManagerImpl
import com.example.petfinderapp.data.network.NetworkProviderImplementation
import com.example.petfinderapp.data.network.PetFinderApiService
import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.data.repositories.AuthorizationRepositoryImplementation
import com.example.petfinderapp.ui.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val NetworkModule = module {

    single { TokenManagerImpl(androidApplication()) }

    single { NetworkProviderImplementation(get()) }

    single {
        (get() as NetworkProviderImplementation).getRetrofit()
            .create(PetFinderApiService::class.java)
    }

}

val ViewModelModule = module {

    viewModel { MainViewModel(get(), get()) }
}

val RepositoryModule = module {

    single { AnimalsRepositoryImplementation((get())) }

    single { AuthorizationRepositoryImplementation(get(), get()) }
}