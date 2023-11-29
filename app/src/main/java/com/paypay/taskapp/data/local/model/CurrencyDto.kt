package com.paypay.taskapp.data.local.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyDto(
    @PrimaryKey(autoGenerate = false)
    val date: Long,
    val currencies: String
)
