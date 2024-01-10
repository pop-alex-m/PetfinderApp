package com.example.petfinderapp.app

import com.example.petfinderapp.data.TokenManager
import com.example.petfinderapp.data.TokenManagerImpl
import com.example.petfinderapp.data.network.AuthorizationProvider
import com.example.petfinderapp.data.network.AuthorizationProviderImplementation
import com.example.petfinderapp.data.network.NetworkProvider
import com.example.petfinderapp.data.network.NetworkProviderImplementation
import com.example.petfinderapp.data.network.services.PetFinderApiService
import com.example.petfinderapp.data.repositories.AnimalsRepository
import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.ui.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val NetworkModule = module {

    single<TokenManager> { TokenManagerImpl(androidApplication()) }

    single<AuthorizationProvider> { AuthorizationProviderImplementation() }

    single<NetworkProvider> { NetworkProviderImplementation(get(), get()) }

    single<PetFinderApiService> {
        (get() as NetworkProvider).getRetrofit()
            .create(PetFinderApiService::class.java)
    }

}

val ViewModelModule = module {

    viewModel { MainViewModel(get()) }
}

val RepositoryModule = module {

    single<AnimalsRepository> { AnimalsRepositoryImplementation((get())) }

}