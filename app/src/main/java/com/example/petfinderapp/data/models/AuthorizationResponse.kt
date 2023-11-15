package com.example.petfinderapp.data.models

import com.google.gson.annotations.SerializedName

data class AuthorizationResponse (
    @SerializedName("token_type")
    val tokenType : String ?= null,
    @SerializedName("expires_in")
    val expiresIn : String ?= null,
    @SerializedName("access_token")
    val accessToken : String ?= null
)