package com.paypay.taskapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paypay.taskapp.data.local.model.CurrencyDto
import com.paypay.taskapp.data.local.model.CurrencyRate


@Dao
interface CurrencyDoa {
    @Query("SELECT * FROM currency")
    suspend fun getAllCurrency(): List<CurrencyDto>

    @Query("UPDATE currency SET date = :newDate, currencies = :newCurrencies WHERE date = :previousDate")
    suspend fun updateNewFetchCurrency(newDate: Long, newCurrencies: String,previousDate:Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: CurrencyDto)


    @Query("SELECT * FROM currency_rate WHERE currencyBase =:baseCurrency")
    suspend fun getCurrenciesRateBase(baseCurrency:String): CurrencyRate?

    @Query("UPDATE currency_rate SET timestamp = :newDate,  currenciesRateSymbole= :symbols WHERE currencyBase = :baseCurrency")
    suspend fun updateNewCurrencyRat(baseCurrency:String,symbols:String,newDate:Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyRate(currency: CurrencyRate)


}