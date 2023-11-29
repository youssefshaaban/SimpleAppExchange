package com.paypay.taskapp.di

import com.paypay.taskapp.data.local.CurrencyDoa
import com.paypay.taskapp.data.remote.ApiService
import com.paypay.taskapp.data.repositeries.CurrencyRepoImp
import com.paypay.taskapp.domain.repositeries.CurrencyRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideMedicineRepo(
        apiService: ApiService,
        currencyDoa: CurrencyDoa,
    ): CurrencyRepo =
        CurrencyRepoImp(apiService, currencyDoa)
}