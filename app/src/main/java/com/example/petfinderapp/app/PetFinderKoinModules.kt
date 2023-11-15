package com.example.petfinderapp.app

import com.example.petfinderapp.data.repositories.AnimalsRepositoryImplementation
import com.example.petfinderapp.data.NetworkServiceImplementation
import com.example.petfinderapp.data.PetFinderApiService
import com.example.petfinderapp.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val NetworkModule = module {

    single { NetworkServiceImplementation }

    single { NetworkServiceImplementation().getRetrofit().create(PetFinderApiService::class.java) }
}

val ViewModelModule = module {

    viewModel { MainViewModel() }
}

val RepositoryModule = module {

    single { AnimalsRepositoryImplementation() }
}