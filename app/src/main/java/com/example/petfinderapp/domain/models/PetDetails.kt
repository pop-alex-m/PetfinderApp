package com.example.petfinderapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PetDetails(
    val id: Int? = null,
    val name: String? = null,
    val gender: String? = null,
    val size: String? = null,
    val breed: String? = null,
    val status: String? = null,
    val distance: String? = null,
    val smallPhotoUrl: String? = null,
    val largePhotoUrl: String? = null
) : Parcelable
