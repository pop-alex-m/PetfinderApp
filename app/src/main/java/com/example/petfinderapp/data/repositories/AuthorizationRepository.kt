package com.example.petfinderapp.data.repositories

import com.example.petfinderapp.data.TokenManagerImpl
import com.example.petfinderapp.data.models.AuthorizationResponse
import com.example.petfinderapp.data.network.PetFinderApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

interface AuthorizationRepository {
    fun refreshAccessToken(): Single<AuthorizationResponse>

    fun isAccessTokenValid(): Boolean

}

class AuthorizationRepositoryImplementation(
    private val apiService: PetFinderApiService,
    private val tokenManager: TokenManagerImpl
) : AuthorizationRepository {

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
        return tokenManager.isTokenValid()
    }
}