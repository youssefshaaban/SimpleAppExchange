package com.paypay.taskapp.ui.Screens.CurrencyChangeScreen


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paypap.taskapp.R
import com.paypay.taskapp.domain.model.Resource
import com.paypay.taskapp.ui.Screens.CurrencyChangeScreen.components.CurrenCyConversionRate
import com.paypay.taskapp.ui.components.ContentTextField
import com.paypay.taskapp.ui.components.CurrencyExposedDropdownMenuBox


@Composable
fun CurrencyChangeScreen() {
    val currencyChangeViewModel: CurrencyChangeViewModel = hiltViewModel()


    when (val currency = currencyChangeViewModel.currenciesState.value) {
        is Resource.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                ContentTextField(
                    modifier = Modifier.padding(top = 20.dp),
                    label = stringResource(id = R.string.amount),
                    text = currencyChangeViewModel.amount.value,
                    onTextChange = { txt -> currencyChangeViewModel.onTextChangeTextAmount(txt) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)) {
                    CurrencyExposedDropdownMenuBox(
                        modifier=Modifier.weight(1f),
                        currencies = currency.data,
                        currencyChangeViewModel.currencySelected.value
                    )
                    { curency -> currencyChangeViewModel.onSelectedItem(curency) }
                    OutlinedButton(modifier = Modifier.padding(10.dp),onClick = { currencyChangeViewModel.onClickChange() }) {
                        Text(text = stringResource(id = R.string.change))
                    }
                }

                when (val state = currencyChangeViewModel.currencyConversionState.value) {
                    is Resource.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Success -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CurrenCyConversionRate(items = state.data)
                        }
                    }
                    is Resource.Error->{
                        
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(id = R.string.error))
                        }
                    }
                    else->{}

                }
            }
        }

        is Resource.Error -> {
            Toast.makeText(LocalContext.current, "something wrong", Toast.LENGTH_SHORT).show()
        }

    }
}