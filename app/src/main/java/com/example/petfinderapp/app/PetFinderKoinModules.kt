package com.example.petfinderapp.app

import com.example.petfinderapp.data.NetworkProviderImplementation
import com.example.petfinderapp.data.PetFinderApiService
import com.example.petfinderapp.data.TokenManager
import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.data.repositories.AuthorizationRepositoryImplementation
import com.example.petfinderapp.ui.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val NetworkModule = module {

    single { TokenManager(androidApplication()) }

    single { NetworkProviderImplementation }

    single { NetworkProviderImplementation().getRetrofit().create(PetFinderApiService::class.java) }

}

val ViewModelModule = module {

    viewModel { MainViewModel() }
}

val RepositoryModule = module {

    single { AnimalsRepositoryImplementation() }

    single { AuthorizationRepositoryImplementation() }
}