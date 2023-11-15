package com.example.petfinderapp.data

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

interface NetworkService {

    fun getRetrofit(): Retrofit

}

open class NetworkServiceImplementation : NetworkService, KoinComponent {

    companion object {
        private const val BASE_URL = "https://api.petfinder.com"
        const val AUTHORIZATION_API_KEY = "D5foHuINtQ6yRvPiupTvSuPqvcFuXEEBYENo3yhHeVwyyY9tc4"
        const val AUTHORIZATION_SECRET = "oiGMBsDZNDOLnCpFqCunQYwZ8xKqCgxzfB1mQryF"

        const val HEADER_AUTHORIZATION_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJENWZvSHVJTnRRNnlSdlBpdXBUdlN1UHF2Y0Z1WEVFQllFTm8zeWhIZVZ3eXlZOXRjNCIsImp0aSI6ImNmZmM2MzQ3YjJhMmQ2ZTA3ZGU0ZDljNzczMzdkNTlhOWU4OTAxY2FhOWM1YTJjOGJkODkyMjVmYjMyODAwNDdjOWQ3ZGNhN2Y3ZTZkMjNiIiwiaWF0IjoxNjk5OTkyNDU5LCJuYmYiOjE2OTk5OTI0NTksImV4cCI6MTY5OTk5NjA1OSwic3ViIjoiIiwic2NvcGVzIjpbXX0.RY380vOCgxksKI8Ru8Hr0kxMFfhHsnm70IZKUtBt125TnGQKhLi3NpKQphVpadeFmAQySgGrmPPEi1lGk7lPtIh9z49i-JjdPw5K5efm0869NIHv2t6QJQnsr5aUg5NYkd_gHq0Y2IMkAqB5jUuIRsZjCsadBL7fKWoBnvzNgWXW-Bu1hpuinP94ZnZ1eWnCvUD-iQs2Ru27Bo8RFbSIuPFc5MX8bf9IkHejFkH_BYrbWItFtvPAGozH8PlvOfb4_srJ90ebxth1NYeKR4Warib0psk20xFlo5KlCBv8I1g_UMHQOrp1rh2w9oc3kbqllD5T9-xa1_v1lXCovTAJhw"
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
        }
    }

    protected open fun configureRetrofit(builder: Retrofit.Builder) {
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
            addHeader("Authorization", "Bearer $HEADER_AUTHORIZATION_TOKEN")
           // addHeader(HEADER_AUTHORIZATION_KEY, HEADER_AUTHORIZATION_SECRET)
        }.build()
    }
}