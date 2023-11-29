package com.paypay.taskapp.data.remote.model

data class CurrencyRateResponse(
    val base: String,
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val rates: Map<String, Float>
)