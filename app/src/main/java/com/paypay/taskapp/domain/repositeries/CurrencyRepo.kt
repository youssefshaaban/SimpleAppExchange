package com.paypay.taskapp.domain.repositeries


import com.paypay.taskapp.domain.model.Resource


interface CurrencyRepo {
   suspend fun getCurrencies(): Resource<List<Pair<String, String>>>

   suspend fun getCurenciesRates(baseCurrency:String): Resource<Map<String,Float>>
}