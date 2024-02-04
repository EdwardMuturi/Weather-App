package com.edwardmuturi.network.utils

import java.lang.Exception
import retrofit2.Response
object ApiCaller {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T?> {
        return try {
            val result = apiCall()

            when (result.isSuccessful) {
                true -> Result.success(result.body())
                false -> Result.failure(IllegalStateException(result.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
