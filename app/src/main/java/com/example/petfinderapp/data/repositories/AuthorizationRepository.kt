package com.example.petfinderapp.data.repositories

import com.example.petfinderapp.data.TokenManager
import com.example.petfinderapp.data.models.AuthorizationResponse
import com.example.petfinderapp.data.network.PetFinderApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AuthorizationRepository {
    fun refreshAccessToken(): Single<AuthorizationResponse>

}

class AuthorizationRepositoryImplementation : AuthorizationRepository, KoinComponent {

    private val apiService: PetFinderApiService by inject()
    private val tokenManager: TokenManager by inject()

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
                authResponse.accessToken?.let { tokenManager.saveToken(it) }
            }
    }

}