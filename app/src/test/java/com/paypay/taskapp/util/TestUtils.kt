package com.paypay.taskapp.util

import com.paypay.taskapp.data.local.model.CurrencyDto
import com.paypay.taskapp.data.local.model.CurrencyRate
import com.paypay.taskapp.data.remote.model.CurrencyRateResponse
import retrofit2.Response


object TestUtil {

    fun defaultCurrency(date: Long = System.currentTimeMillis() - 86400000) =
        CurrencyDto(date = date, currencies = "{\"USD\":\"united stat\"}")

    fun defaultCurrencyRate(date: Long = System.currentTimeMillis() - 86400000) =
        CurrencyRate(currencyBase = "USD", timestamp = date, currenciesRateSymbole = "{\"EGP\":30.899}")

    fun createMockCurrencyRateResponse(date: Long=System.currentTimeMillis() - 86400000): Response<CurrencyRateResponse> {
        // Create a map of currencies for the mock response

        // Create a Response object with your mock data
        return Response.success(CurrencyRateResponse("USD","","", timestamp = date, rates = mapOf("EGP" to 30.8777f)))
    }

    fun createMockCurrencyResponse(): Response<Map<String, String>> {
        // Create a map of currencies for the mock response
        val currencies = mapOf(
            "USD" to "United States Dollar",
            "EUR" to "Euro",
            // Add more currencies as needed
        )

        // Create a Response object with your mock data
        return Response.success(currencies)
    }
}
