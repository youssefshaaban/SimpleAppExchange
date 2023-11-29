package com.paypay.taskapp.data.util



import com.paypay.taskapp.domain.model.Failure
import com.paypay.taskapp.domain.model.Resource
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.UnknownHostException


suspend fun <R> apiCall(call: suspend () -> Response<R>): Resource<R> {
    try {
        val response = call()
        val responseCode = response.code()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }
        val errorStr = response.errorBody()?.string()
        return when (responseCode) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> Resource.Error(Failure.UnAuthorize)
            HttpURLConnection.HTTP_NOT_FOUND,
            HttpURLConnection.HTTP_BAD_REQUEST,
            HttpURLConnection.HTTP_FORBIDDEN, HttpURLConnection.HTTP_UNAVAILABLE, HttpURLConnection.HTTP_INTERNAL_ERROR -> Resource.Error(
                error = Failure.ServerError(errorStr)
            )

            else -> Resource.Error(error = Failure.UnknownError(errorStr))
        }

    } catch (e: Exception) {
        return when (e) {
            is UnknownHostException -> Resource.Error(error = Failure.NetworkConnection)
            else -> {
                Resource.Error(error = Failure.UnknownError(e.toString()))
            }
        }
    }
}