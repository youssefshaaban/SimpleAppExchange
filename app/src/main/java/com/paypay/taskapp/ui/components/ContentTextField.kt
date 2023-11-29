package com.paypay.taskapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentTextField(
    modifier: Modifier = Modifier, label: String, text: String, onTextChange: (String) -> Unit,
    isError: Boolean = false, message: String = "",keyboardOptions: KeyboardOptions=KeyboardOptions.Default
) {
    TextField(value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        isError = isError,

        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        })
}
