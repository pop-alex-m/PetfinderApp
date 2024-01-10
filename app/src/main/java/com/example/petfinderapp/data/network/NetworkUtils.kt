package com.example.petfinderapp.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException


class NetworkUtils {
    companion object {

        private const val TAG = "NetworkUtils"
        suspend fun <T> safeApiCall(
            dispatcher: CoroutineDispatcher,
            apiCall: suspend () -> T
        ): ResultWrapper<T> {
            return withContext(dispatcher) {
                try {
                    val result = apiCall.invoke()
                    ResultWrapper.Success(result)
                } catch (exception: Exception) {
                    when (exception) {
                        is UnknownHostException -> {
                            ResultWrapper.NoConnectionError
                        }

                        is HttpException -> {
                            when {
                                // Unauthorized exception
                                exception.code() == 401 -> {
                                    ResultWrapper.AuthorizationNotFoundError
                                }
                                // Internal server error
                                (exception.code() == 501 || exception.code() == 500) -> {
                                    ResultWrapper.InternalServerError
                                }

                                (exception.code() == 429) -> {
                                    ResultWrapper.RateLimitExceeded
                                }
                                // Generic server error
                                else -> {
                                    ResultWrapper.GenericError(exception.code(), exception.message)
                                }
                            }
                        }

                        else -> {
                            Log.e(TAG, "Received error from http client " + exception.message)
                            ResultWrapper.GenericError(null, exception.message)
                        }
                    }
                }
            }
        }
    }

}
