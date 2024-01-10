package com.example.petfinderapp.data.network.services

import com.example.petfinderapp.data.models.AuthorizationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthorizationService {

    @FormUrlEncoded
    @POST("v2/oauth2/token")
    suspend fun authorize(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): AuthorizationResponse
}