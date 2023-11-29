package com.paypay.taskapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rate")
data class CurrencyRate(@PrimaryKey(autoGenerate = false) val currencyBase: String, val  currenciesRateSymbole:String, val timestamp:Long)
