package com.example.petfinderapp.data.models

data class Animal (
    val name: String? = null,
    val gender: String? = null,
    val size: String? = null,
    val breed: Breed? = null,
    val status: String? = null,
    val distance: String? = null,
    val photos: List<Photo>? = null
)