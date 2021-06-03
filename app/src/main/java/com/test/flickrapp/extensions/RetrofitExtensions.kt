package com.test.flickrapp.extensions

import android.accounts.NetworkErrorException
import com.google.gson.Gson
import retrofit2.Call

@Throws(NetworkErrorException::class, BadResponseException::class)
fun <T> Call<T>.executeHandlingNetworkError(): T {
    try {
        val response = execute()

        if (response.isSuccessful && response.body() != null) {

            return response.body()!!
        } else {
            throw BadResponseException(response.code(), response.message())
        }

    } catch (e: Exception) {
        e.fillInStackTrace()
        e.printStackTrace()
        throw NetworkErrorException()
    }
}


@Throws(NetworkErrorException::class, BadResponseException::class)
fun <T> Call<T>.executeHandlingNetworkErrorWithoutResponse() {
    try {
        val response = execute()

        if (!response.isSuccessful) {
            throw BadResponseException(response.code(), response.message())
        }
    } catch (e: Exception) {
        throw NetworkErrorException()
    }
}

class BadResponseException(val code: Int, message: String) : Throwable(message)