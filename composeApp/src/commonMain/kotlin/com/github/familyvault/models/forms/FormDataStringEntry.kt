package com.github.familyvault.models.forms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FormDataStringEntry : BaseFormDataEntry() {
    var value: String by mutableStateOf("")
}
