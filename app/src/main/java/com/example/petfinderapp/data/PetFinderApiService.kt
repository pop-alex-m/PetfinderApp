package com.example.petfinderapp.data

import com.example.petfinderapp.data.models.PetResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PetFinderApiService {

    // https://api.petfinder.com/v2/animals?type=dog&page=2
    @GET("v2/animals")
    fun getListOfAnimals(@Query("type") type :String, @Query("page") page : Int) : Single<PetResponse>
}