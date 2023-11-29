package com.paypay.taskapp.domain.interactors


import com.paypay.taskapp.domain.model.CurrencyConversion
import com.paypay.taskapp.domain.model.Resource
import com.paypay.taskapp.domain.repositeries.CurrencyRepo
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class GetCurrenciesConversionTest {

    private lateinit var currencyRepo: CurrencyRepo
    private lateinit var getCurrenciesConversion: GetCurrenciesConversion

    @Before
    fun setUp() {
        currencyRepo = mockk()
        getCurrenciesConversion = GetCurrenciesConversion(currencyRepo)
    }


    @Test
    fun `test getCurrenciesConversion`() = runTest {
        // Prepare test data
        val selectedCurrency = "USD"
        val amount = 100f
        val dataMap = mapOf("EUR" to 0.85f, "GBP" to 0.75f) // Mocked data

        coEvery { currencyRepo.getCurenciesRates(baseCurrency = selectedCurrency) } returns  Resource.Success(dataMap)


        // Invoke the function under test
        val result = getCurrenciesConversion(selectedCurrency, amount)

        // Verify the interactions and assertions
        coVerify { (currencyRepo).getCurenciesRates(baseCurrency = selectedCurrency) }
        assertEquals(result, Resource.Success(listOf(
            CurrencyConversion("EUR", 85f),
            CurrencyConversion("GBP", 75f)
        )))
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

}
