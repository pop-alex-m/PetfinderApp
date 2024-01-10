package com.example.petfinderapp.data.network

import com.example.petfinderapp.data.TokenManager
import com.example.petfinderapp.data.models.AuthorizationResponse
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

interface NetworkProvider {

    fun getRetrofit(): Retrofit

}

class NetworkProviderImplementation(
    private val tokenManager: TokenManager,
    private val authorizationProvider: AuthorizationProvider
) :
    NetworkProvider {

    companion object {
        private const val BASE_URL = "https://api.petfinder.com"
        private const val tokenBearer = "Bearer"
        const val grantType = "client_credentials"
        const val clientId = "D5foHuINtQ6yRvPiupTvSuPqvcFuXEEBYENo3yhHeVwyyY9tc4"
        const val clientSecret = "oiGMBsDZNDOLnCpFqCunQYwZ8xKqCgxzfB1mQryF"
    }

    override fun getRetrofit(): Retrofit {
        return Retrofit.Builder().run {
            configureRetrofit(this)
            baseUrl(BASE_URL)
            client(buildHttpClient())
            build()
        }
    }

    private fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            configureHttpClient(this)
            build()
        }
    }

    private fun configureHttpClient(builder: OkHttpClient.Builder) {
        with(builder) {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addNetworkInterceptor(buildDefaultHeaderInterceptor())
            authenticator(buildTokenRefreshInterceptor())
        }
    }

    private fun configureRetrofit(builder: Retrofit.Builder) {
        with(builder) {
            addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

    private fun buildDefaultHeaderInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            try {
                val original = chain.request()
                val request: Request = buildAuthorisationHeaders(original)
                chain.proceed(request)
            } catch (ex: IOException) {
                throw ex
            }
        }
    }

    private fun buildAuthorisationHeaders(original: Request): Request {
        return original.newBuilder().apply {
            addHeader("Authorization", "$tokenBearer ${tokenManager.getToken()}")
        }.build()
    }

    private fun buildTokenRefreshInterceptor(): Authenticator {
        return Authenticator { route, response ->
            val authorizationResponse = getUpdatedToken()
            if (authorizationResponse.accessToken != null && authorizationResponse.expiresIn != null) {
                tokenManager.saveToken(
                    authorizationResponse.accessToken,
                    authorizationResponse.expiresIn
                )
                response.request.newBuilder()
                    .header("Authorization", "$tokenBearer ${tokenManager.getToken()} ")
                    .build()
            } else null
        }
    }

    private fun getUpdatedToken(): AuthorizationResponse {
        return runBlocking {
            authorizationProvider.getAuthorizationService()
                .authorize(grantType, clientId, clientSecret)
        }
    }
}