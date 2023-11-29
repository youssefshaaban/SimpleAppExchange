package com.paypay.taskapp.ui.Screens.CurrencyChangeScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypay.taskapp.domain.interactors.GetCurrenciesConversion
import com.paypay.taskapp.domain.interactors.GetCurrencyUseCase
import com.paypay.taskapp.domain.model.CurrencyConversion
import com.paypay.taskapp.domain.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyChangeViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getCurrenciesConversion: GetCurrenciesConversion,
) :
    ViewModel() {
    val amount = mutableStateOf("")
    val currencySelected = mutableStateOf("")

    private val _currenciesState = mutableStateOf<Resource<List<Pair<String, String>>>>(Resource.Loading)
    val currenciesState: MutableState<Resource<List<Pair<String, String>>>> = _currenciesState
    private val _currencyConversionState = mutableStateOf<Resource<List<CurrencyConversion>>?>(null)
    val currencyConversionState: MutableState<Resource<List<CurrencyConversion>>?> = _currencyConversionState

    init {

        viewModelScope.launch {
            currenciesState.value= getCurrencyUseCase()
        }

    }

    fun onTextChangeTextAmount(value: String) {
        amount.value = value
    }

    fun onClickChange(){
        if (amount.value.isNotEmpty()&&currencySelected.value.isNotEmpty()) {
            viewModelScope.launch {
                _currencyConversionState.value=getCurrenciesConversion(currencySelected.value, amount = amount.value.toFloat())
            }
        }
    }

    fun onSelectedItem(item: String) {
        currencySelected.value = item
        if (amount.value.isNotEmpty()) {
            viewModelScope.launch {
                _currencyConversionState.value=getCurrenciesConversion(item, amount = amount.value.toFloat())
            }
        }
    }
}