package com.paypay.taskapp.data.repositories

import com.paypay.taskapp.data.local.CurrencyDoa
import com.paypay.taskapp.data.remote.ApiService
import com.paypay.taskapp.data.repositeries.CurrencyRepoImp
import com.paypay.taskapp.domain.model.Resource
import com.paypay.taskapp.util.TestUtil.createMockCurrencyRateResponse
import com.paypay.taskapp.util.TestUtil.createMockCurrencyResponse
import com.paypay.taskapp.util.TestUtil.defaultCurrency
import com.paypay.taskapp.util.TestUtil.defaultCurrencyRate
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CurrencyRepoImpTest {
    var apiService: ApiService = mockk()


    var currencyDao: CurrencyDoa = mockk()



    lateinit var currencyRepo: CurrencyRepoImp

    @Before
    fun setUp() {
        currencyRepo = CurrencyRepoImp(apiService, currencyDao)
    }

    @Test
    fun `test getCurrencies() with stale data then call api update data`() {

        runTest {
            // Mock behavior for stale data
            coEvery { currencyDao.getAllCurrency() } returns listOf(
                defaultCurrency()
            ) // Simulate having data in DB
            coEvery { currencyDao.updateNewFetchCurrency(any(),any(),any()) } just runs
            // Verify that appropriate methods were called
            coEvery { apiService.getCurrency() } returns  createMockCurrencyResponse()

            // Mock API service response

            val result = currencyRepo.getCurrencies()

            coVerify { apiService.getCurrency() }
            coVerify { currencyDao.updateNewFetchCurrency(any(), any(), any()) }

            // Assert the result
            assertTrue(result is Resource.Success)
        }
    }

    @Test
    fun `test getCurrencies() with Failure api and currencies in local then return data from local `() {

        runTest {
            // Mock behavior for stale data
            val default=defaultCurrency()
            coEvery { currencyDao.getAllCurrency() } returns listOf(
               default
            ) // Simulate having data in DB
            // Verify that appropriate methods were called
            coEvery { apiService.getCurrency() } returns  Response.error(500,"str".toResponseBody())

            // Mock API service response

            val result = currencyRepo.getCurrencies()

            coVerify { apiService.getCurrency() }

            // Assert the result
            assertTrue(result is Resource.Success)
        }
    }


    @Test
    fun `test getCurrencies() with Failure api and currencies  is empty in local then return error `() {

        runTest {
            // Mock behavior for stale data
            coEvery { currencyDao.getAllCurrency() } returns listOf(
            ) // Simulate having data in DB
            // Verify that appropriate methods were called
            coEvery { apiService.getCurrency() } returns  Response.error(500,"str".toResponseBody())

            // Mock API service response

            val result = currencyRepo.getCurrencies()

            coVerify { apiService.getCurrency() }

            // Assert the result
            assertTrue(result is Resource.Error)
        }
    }


    @Test
    fun `test getCurrencies() without stale data then get currency from local `() {

        runTest {
            // Mock behavior for stale data
            coEvery { currencyDao.getAllCurrency() } returns listOf(
                defaultCurrency(System.currentTimeMillis() + 86400000)
            ) // Simulate having data in DB
            val result = currencyRepo.getCurrencies()
            // Assert the result
            assertTrue(result is Resource.Success)
        }
    }


    @Test
    fun `test getCurenciesRate() with stale data then call api update data`() {

        runTest {
            // Mock behavior for stale data
            coEvery { currencyDao.getCurrenciesRateBase(any()) } returns defaultCurrencyRate()
            coEvery { currencyDao.updateNewCurrencyRat(any(),any(),any()) } just runs
            // Verify that appropriate methods were called
            coEvery { apiService.getCurrencyRate("USD") } returns  createMockCurrencyRateResponse()

            // Mock API service response

            val result = currencyRepo.getCurenciesRates("USD")

            coVerify { apiService.getCurrencyRate("USD") }
            coVerify { currencyDao.updateNewCurrencyRat("USD", "{\"EGP\":30.8777}", any()) }

            // Assert the result
            assertTrue(result is Resource.Success)
            assertEquals((result as Resource.Success).data.toString(), mapOf("EGP" to 30.8777).toString())
        }
    }

    @Test
    fun `test getCurenciesRate() with Failure api and currency conversion in local then return data from local `() {

        runTest {
            coEvery { currencyDao.getCurrenciesRateBase(any()) } returns defaultCurrencyRate()
            // Verify that appropriate methods were called
            coEvery { apiService.getCurrencyRate(any()) } returns  Response.error(500,"str".toResponseBody())

            // Mock API service response

            val result = currencyRepo.getCurenciesRates("USD")

            coVerify { apiService.getCurrencyRate("USD") }

            // Assert the result
            assertTrue(result is Resource.Success)
        }
    }


    @Test
    fun `test getCurenciesRate() with Failure api and currencies  is empty in local then return error `() {

        runTest {
            // Mock behavior for stale data
            coEvery { currencyDao.getCurrenciesRateBase(any()) } returns  null
            // Verify that appropriate methods were called
            coEvery { apiService.getCurrencyRate(any()) } returns  Response.error(500,"str".toResponseBody())

            // Mock API service response

            val result = currencyRepo.getCurenciesRates("USD")

            coVerify { apiService.getCurrencyRate("USD") }

            // Assert the result
            assertTrue(result is Resource.Error)
        }
    }


    @Test
    fun `test getCurrenciesRate() without stale data then get currency rate from local `() {
        val defaultRate=defaultCurrencyRate(System.currentTimeMillis() + 86400000)
        runTest {
            // Mock behavior for stale data
            coEvery { currencyDao.getCurrenciesRateBase(any()) } returns defaultRate


            // Mock API service response

            val result = currencyRepo.getCurenciesRates("USD")

            // Assert the result
            assertTrue(result is Resource.Success)
            assertEquals(
                (result as Resource.Success).data.keys.toList().get(0),
                "EGP"
            )
        }
    }

    @After
    fun clearDown(){
        clearAllMocks()
    }
}
