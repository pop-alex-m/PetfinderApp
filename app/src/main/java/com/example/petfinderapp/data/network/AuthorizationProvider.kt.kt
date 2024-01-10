package com.example.petfinderapp.data.network

import com.example.petfinderapp.data.network.services.AuthorizationService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AuthorizationProvider {

    fun getAuthorizationService(): AuthorizationService

}

class AuthorizationProviderImplementation : AuthorizationProvider {

    companion object {
        private const val BASE_URL = "https://api.petfinder.com"
    }

    override fun getAuthorizationService(): AuthorizationService {
        return getRetrofit().create(AuthorizationService::class.java)
    }

    private fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}