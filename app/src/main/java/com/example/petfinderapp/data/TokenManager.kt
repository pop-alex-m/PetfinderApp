package com.example.petfinderapp.data

import android.content.Context

class TokenManager(private val context : Context) {
    companion object {
        const val prefsFileName = "PetFinderPreferences"
        const val accessToken = "accessToken"
    }

    fun saveToken(token: String) {
        val editor = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        editor.edit().putString(accessToken, token).apply()
    }

    fun getToken() : String {
        return context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
            .getString(accessToken, "") ?: ""
    }

}