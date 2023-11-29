package com.paypay.taskapp.data.remote


import com.paypap.taskapp.BuildConfig
import com.paypay.taskapp.data.remote.model.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/currencies.json")
    suspend fun getCurrency(): Response<Map<String, String>>

    @GET("api/latest.json?app_id=${BuildConfig.API_KEY_ECHANGE}")
    suspend fun getCurrencyRate(
        @Query("base") base: String
    ): Response<CurrencyRateResponse>


}