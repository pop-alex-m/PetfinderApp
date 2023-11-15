package com.example.petfinderapp.app

import android.app.Application
import org.koin.core.context.startKoin

class PetFinderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(NetworkModule, ViewModelModule, RepositoryModule)
        }
    }
}