package com.paypay.taskapp.domain.interactors


import com.paypay.taskapp.domain.model.Resource
import com.paypay.taskapp.domain.repositeries.CurrencyRepo
import javax.inject.Inject

class GetCurrencyUseCase @Inject constructor(private val currencyRepo: CurrencyRepo) {
    suspend operator fun invoke(): Resource<List<Pair<String, String>>> =
        currencyRepo.getCurrencies()
}

