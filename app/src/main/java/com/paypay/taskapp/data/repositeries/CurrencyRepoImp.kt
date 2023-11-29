package com.paypay.taskapp.data.repositeries


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paypay.taskapp.data.local.CurrencyDoa
import com.paypay.taskapp.data.local.model.CurrencyDto
import com.paypay.taskapp.data.local.model.CurrencyRate
import com.paypay.taskapp.data.remote.ApiService
import com.paypay.taskapp.data.util.apiCall
import com.paypay.taskapp.data.util.isDateStale
import com.paypay.taskapp.domain.model.Resource
import com.paypay.taskapp.domain.repositeries.CurrencyRepo
import java.lang.reflect.Type
import javax.inject.Inject


class CurrencyRepoImp @Inject constructor(
    private val apiService: ApiService,
    private val currencyDoa: CurrencyDoa,
) : CurrencyRepo {
    override suspend fun getCurrencies(): Resource<List<Pair<String, String>>> {
        val currency = currencyDoa.getAllCurrency()
        if (currency.isNotEmpty() && !isDateStale(currency[0].date)) {
            val type: Type = object : TypeToken<Map<String, String>>() {}.type
            val data = Gson().fromJson<Map<String, String>>(currency[0].currencies, type)
            return Resource.Success(data.toList())
        } else {
            val result = apiCall {
                apiService.getCurrency()
            }
            return when (result) {
                is Resource.Success -> {
                    val data = result.data
                    val currentTime = System.currentTimeMillis()
                    val gson = Gson()
                    val jsonStr = gson.toJson(data)
                    if (currency.isNotEmpty()) {
                        currencyDoa.updateNewFetchCurrency(currentTime, jsonStr, currency[0].date)
                    } else {
                        currencyDoa.insertCurrency(
                            CurrencyDto(
                                currentTime,
                                jsonStr
                            )
                        )
                    }
                    Resource.Success(data.toList())
                }

                is Resource.Error -> {
                    if (currency.isNotEmpty()) {
                        val type: Type = object : TypeToken<Map<String, String>>() {}.type
                        val data =
                            Gson().fromJson<Map<String, String>>(currency[0].currencies, type)
                        Resource.Success(data.toList())
                    } else {
                        Resource.Error(result.error)
                    }
                }

                is Resource.Loading -> {
                    Resource.Loading
                }
            }
        }

    }

    override suspend fun getCurenciesRates(
        baseCurrency: String
    ): Resource<Map<String, Float>> {

        val currency = currencyDoa.getCurrenciesRateBase(baseCurrency)
        if (currency != null && !isDateStale(currency.timestamp)) {
            val type: Type = object : TypeToken<Map<String, Float>>() {}.type
            val data = Gson().fromJson<Map<String, Float>>(currency.currenciesRateSymbole, type)
            return Resource.Success(data)
        } else {
            val result = apiCall {
                apiService.getCurrencyRate(baseCurrency)
            }
            return when (result) {
                is Resource.Success -> {
                    val data = result.data.rates
                    val currentTime = System.currentTimeMillis()
                    val gson = Gson()
                    val jsonStr = gson.toJson(data)
                    if (currency != null) {
                        currencyDoa.updateNewCurrencyRat(
                            baseCurrency,
                            symbols = jsonStr,
                            currentTime
                        )
                    } else {
                        currencyDoa.insertCurrencyRate(
                            CurrencyRate(
                                baseCurrency, jsonStr, currentTime
                            )
                        )
                    }
                    Resource.Success(data)
                }

                is Resource.Error -> {
                    if (currency != null) {
                        val type: Type = object : TypeToken<Map<String, Float>>() {}.type
                        val data = Gson().fromJson<Map<String, Float>>(
                            currency.currenciesRateSymbole,
                            type
                        )
                        Resource.Success(data)
                    } else
                        Resource.Error(result.error)
                }

                is Resource.Loading -> {
                    Resource.Loading
                }
            }
        }
    }

}