package com.example.petfinderapp.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

interface EncryptedSharedPrefs {

    fun getEncryptedSharedPrefs(): SharedPreferences
}

class EncryptedSharedPrefsImpl(private val context: Context) : EncryptedSharedPrefs {
    private val masterKey
        get() = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    override fun getEncryptedSharedPrefs(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "pet_finder_encrypted_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}