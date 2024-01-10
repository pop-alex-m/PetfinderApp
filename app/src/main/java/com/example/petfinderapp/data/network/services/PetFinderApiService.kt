package com.example.petfinderapp.data.network.services

import com.example.petfinderapp.data.models.PetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PetFinderApiService {
    @GET("v2/animals")
    suspend fun getListOfAnimals(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PetResponse

}