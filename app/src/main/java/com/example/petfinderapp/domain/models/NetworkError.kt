package com.example.petfinderapp.domain.models

enum class NetworkError(val errorMessage : String) {
    AUTHENTICATION_ERROR("Authentication failed"),
    CONNECTIVITY_ERROR("Could not connect to the internet, please try again later"),
    GENERIC_ERROR("Unknown error")
}