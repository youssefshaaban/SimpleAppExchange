package com.paypay.taskapp.data.local


import com.paypay.taskapp.data.local.model.CurrencyRate
import com.paypay.taskapp.util.TestUtil.defaultCurrency
import com.paypay.taskapp.util.TestUtil.defaultCurrencyRate
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CurrencyDoaTest {

    private var currencyDoa = mockk<CurrencyDoa>()


    @Test
    fun ` test getAllCurrency isEmpty `(){
        runTest {
            coEvery { currencyDoa.getAllCurrency() } returns emptyList()
            Assert.assertEquals(0, currencyDoa.getAllCurrency().size)
        }
    }

    @Test
    fun `test getAllCurrency isNotEmpty`(){
        runTest {
            coEvery {currencyDoa.getAllCurrency()  } returns listOf(defaultCurrency())
            Assert.assertEquals(1, currencyDoa.getAllCurrency().size)
        }
    }

    @Test
    fun `test insert currency in db`(){
        runTest {
            coEvery {currencyDoa.insertCurrency(any())  } just runs
            currencyDoa.insertCurrency(defaultCurrency())
            coVerify { currencyDoa.insertCurrency(any()) }
        }
    }

    @Test
    fun `test update currency in db`(){
        runTest {
            coEvery {currencyDoa.updateNewFetchCurrency(any(),any(),any())  } just runs
            currencyDoa.updateNewFetchCurrency(1L,"cv",12L)
            coVerify { currencyDoa.updateNewFetchCurrency(1L,"cv",12L) }
        }
    }

    @Test
    fun `test update currency conversion rate in db `(){
        runTest {
            coEvery {currencyDoa.updateNewCurrencyRat(any(),any(),any())  } just runs
            currencyDoa.updateNewCurrencyRat("USD","{\"EGP\":30.899}",12L)
            coVerify { currencyDoa.updateNewCurrencyRat("USD","{\"EGP\":30.899}",12L) }
        }
    }

    @Test
    fun `test insert currency conversion rate to db `(){
        runTest {
            coEvery {currencyDoa.insertCurrencyRate(any())  } just runs
            currencyDoa.insertCurrencyRate(defaultCurrencyRate())
            coVerify { currencyDoa.insertCurrencyRate(any()) }
        }
    }

    @Test
    fun `test get currency conversion in db`(){
        runTest {
            coEvery {currencyDoa.getCurrenciesRateBase("USD")  } returns CurrencyRate("USD","{\"EGP\":3089}",34L)
            val result =currencyDoa.getCurrenciesRateBase("USD")
            Assert.assertEquals(result?.currenciesRateSymbole,"{\"EGP\":3089}")
        }
    }

    @After
    fun clearDown(){
        clearAllMocks()
    }
}