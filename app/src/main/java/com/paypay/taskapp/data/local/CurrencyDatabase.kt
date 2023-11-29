package com.paypay.taskapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paypay.taskapp.data.local.model.CurrencyDto
import com.paypay.taskapp.data.local.model.CurrencyRate

@Database(entities = [CurrencyDto::class, CurrencyRate::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDoa
}
