import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.paypay.taskapp.domain.interactors.GetCurrenciesConversion
import com.paypay.taskapp.domain.interactors.GetCurrencyUseCase
import com.paypay.taskapp.domain.model.CurrencyConversion
import com.paypay.taskapp.domain.model.Failure
import com.paypay.taskapp.domain.model.Resource
import com.paypay.taskapp.ui.Screens.CurrencyChangeScreen.CurrencyChangeViewModel
import com.paypay.taskapp.util.MainCoroutineRuleTest

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyChangeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRuleTest = MainCoroutineRuleTest()


    val getCurrencyUseCase: GetCurrencyUseCase = mockk()

    val getCurrenciesConversion: GetCurrenciesConversion = mockk()

    private lateinit var viewModel: CurrencyChangeViewModel


    @Before
    fun setup() {
        coEvery { getCurrencyUseCase() } answers {
            Resource.Loading
        }
    }

    @Test
    fun `test initial loading state`() = runTest {
        coEvery { getCurrencyUseCase.invoke() } returns Resource.Loading
        viewModel = CurrencyChangeViewModel(getCurrencyUseCase, getCurrenciesConversion)
        assertEquals(Resource.Loading, viewModel.currenciesState.value)
    }

    @Test
    fun `test successful data retrieval`() = runTest {
        val currenciesMap = mapOf("USD" to "United States Dollar", "EUR" to "Euro")
        val successResource = Resource.Success(currenciesMap.toList())
        coEvery { getCurrencyUseCase.invoke() } returns successResource
        viewModel = CurrencyChangeViewModel(getCurrencyUseCase, getCurrenciesConversion)
        assertEquals(successResource, viewModel.currenciesState.value)
    }

    @Test
    fun `test error state call get currencies `() = runTest {
        val errror=Resource.Error(Failure.NetworkConnection)
        coEvery { getCurrencyUseCase.invoke() } returns errror
        viewModel = CurrencyChangeViewModel(getCurrencyUseCase, getCurrenciesConversion)
        assertEquals(errror, viewModel.currenciesState.value)
    }

    @Test
    fun `test onTextChangeTextAmount`() {
        viewModel = CurrencyChangeViewModel(getCurrencyUseCase, getCurrenciesConversion)
        viewModel.onTextChangeTextAmount("100")
        assertEquals("100", viewModel.amount.value)
    }

    @Test
    fun `test onSelectedItem call conversion currency when success return list of conversion`() {
        val selectedCurrency = "USD"

        viewModel = CurrencyChangeViewModel(getCurrencyUseCase, getCurrenciesConversion)
        viewModel.onTextChangeTextAmount("22")
        val currenciesConversion =
            listOf(CurrencyConversion("EUR", viewModel.amount.value.toInt() * .92f))
        val successResource=Resource.Success(currenciesConversion)
        coEvery {
            getCurrenciesConversion(any(),any())
        } returns successResource


        viewModel.onSelectedItem(selectedCurrency)

        coVerify { getCurrenciesConversion("USD",22f) }
        assertEquals(selectedCurrency, viewModel.currencySelected.value)
        assertEquals(successResource, viewModel.currencyConversionState.value)
    }

    @Test
    fun `test error state call get conversion currency `() = runTest {
        coEvery { getCurrencyUseCase() } returns Resource.Success(listOf())
        viewModel = CurrencyChangeViewModel(getCurrencyUseCase, getCurrenciesConversion)
        val errror=Resource.Error(Failure.NetworkConnection)
        coEvery { getCurrenciesConversion.invoke(any(),any()) } returns errror
        viewModel.onTextChangeTextAmount("22")
        viewModel.onSelectedItem("USD")
        assertEquals(errror, viewModel.currencyConversionState.value)
    }



    @After
    fun tearDown(){
        clearAllMocks()
    }
}
