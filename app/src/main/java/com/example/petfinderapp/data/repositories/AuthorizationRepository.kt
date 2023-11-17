package com.example.petfinderapp.data.repositories

import com.example.petfinderapp.data.TokenManagerImpl
import com.example.petfinderapp.data.models.AuthorizationResponse
import com.example.petfinderapp.data.network.PetFinderApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AuthorizationRepository {
    fun refreshAccessToken(): Single<AuthorizationResponse>

    fun isAccessTokenValid(): Boolean

}

class AuthorizationRepositoryImplementation : AuthorizationRepository, KoinComponent {

    private val apiService: PetFinderApiService by inject()
    private val tokenManager: TokenManagerImpl by inject()

    companion object {
        const val grantType = "client_credentials"
        const val clientId = "D5foHuINtQ6yRvPiupTvSuPqvcFuXEEBYENo3yhHeVwyyY9tc4"
        const val clientSecret = "oiGMBsDZNDOLnCpFqCunQYwZ8xKqCgxzfB1mQryF"
    }

    override fun refreshAccessToken(): Single<AuthorizationResponse> {
        return apiService.authorize(grantType, clientId, clientSecret)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { authResponse ->
                val token = authResponse.accessToken
                val expiresIn = authResponse.expiresIn
                if (token != null && expiresIn != null) {
                    tokenManager.saveToken(token, expiresIn)
                }
            }
    }

    override fun isAccessTokenValid(): Boolean {
        val accessToken = tokenManager.getToken()
        val timeStamp = tokenManager.getTokenTimeStamp()
        val expiresIn = tokenManager.getTokenExpiresIn()
        return accessToken.isNotEmpty() && ((timeStamp + expiresIn) > System.currentTimeMillis())
    }
}