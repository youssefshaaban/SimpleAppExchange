package com.paypay.taskapp.di

import android.content.Context
import androidx.room.Room
import com.paypay.taskapp.data.local.CurrencyDatabase
import com.paypay.taskapp.data.local.CurrencyDoa

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): CurrencyDatabase {
        return Room.databaseBuilder(
            appContext,
            CurrencyDatabase::class.java,
            "task_database"
        ).build()
    }


    @Provides
    fun provideTaskDao(database: CurrencyDatabase): CurrencyDoa {
        return database.currencyDao()
    }

    @Provides
    @Named("dispatcherMain")
    fun provideDispatcherMain(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}