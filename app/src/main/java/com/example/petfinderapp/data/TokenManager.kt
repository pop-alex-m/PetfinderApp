package com.example.petfinderapp.data

import android.content.Context

interface TokeManager {
    fun saveToken(token: String, expiresIn: String)

    fun getToken(): String

    fun getTokenTimeStamp(): Long
    fun getTokenExpiresIn(): Long
}

class TokenManagerImpl(private val context: Context) : TokeManager {
    companion object {
        const val prefsFileName = "PetFinderPreferences"
        const val accessToken = "accessToken"
        const val accessTokenTimestampMs = "accessTokenTimestamp"
        const val accessTokenExpiresIn = "accessTokenExpiresIn"
    }

    override fun saveToken(token: String, expiresIn: String) {
        val editor = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
        with(editor.edit()) {
            putString(accessToken, token)
            putLong(accessTokenExpiresIn, expiresIn.toLong())
            putLong(accessTokenTimestampMs, System.currentTimeMillis())
        }.apply()
    }

    override fun getToken(): String {
        return context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
            .getString(accessToken, "") ?: ""
    }

    override fun getTokenTimeStamp(): Long {
        return context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
            .getLong(accessTokenTimestampMs, 0)
    }

    override fun getTokenExpiresIn(): Long {
        return context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
            .getLong(accessTokenExpiresIn, 0)
    }

}