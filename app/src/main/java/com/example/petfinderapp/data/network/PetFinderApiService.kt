package com.example.petfinderapp.data.network

import com.example.petfinderapp.data.models.AuthorizationResponse
import com.example.petfinderapp.data.models.PetResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PetFinderApiService {

    @GET("v2/animals")
    fun getListOfAnimals(@Query("type") type: String, @Query("page") page: Int): Single<PetResponse>

    @FormUrlEncoded
    @POST("v2/oauth2/token")
    fun authorize(@Field("grant_type") grantType : String,
                  @Field("client_id") clientId : String,
                  @Field("client_secret") clientSecret : String) : Single<AuthorizationResponse>
}