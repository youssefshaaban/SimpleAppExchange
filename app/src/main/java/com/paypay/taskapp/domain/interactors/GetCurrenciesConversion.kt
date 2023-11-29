package com.paypay.taskapp.domain.interactors


import com.paypay.taskapp.domain.model.CurrencyConversion
import com.paypay.taskapp.domain.model.Resource
import com.paypay.taskapp.domain.repositeries.CurrencyRepo
import javax.inject.Inject


class GetCurrenciesConversion @Inject constructor(val currencyRepo: CurrencyRepo) {
    suspend operator fun invoke(
        selectedCurrency: String,
        amount:Float
    ): Resource<List<CurrencyConversion>> {
        return when (val result = currencyRepo.getCurenciesRates(baseCurrency = selectedCurrency)) {
            is Resource.Success -> {
                val data=result.data
                val listCurrencyConversion= arrayListOf<CurrencyConversion>()
                data.keys.forEach { key->
                    listCurrencyConversion.add(CurrencyConversion(currency = key,(amount * data.getOrDefault(key = key,0f)) ))
                }
                Resource.Success(listCurrencyConversion)
            }

            is Resource.Error -> {
                Resource.Error(result.error)
            }

            else-> {
                Resource.Loading
            }
        }
    }
}