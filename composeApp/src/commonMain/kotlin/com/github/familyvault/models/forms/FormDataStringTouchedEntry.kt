package com.github.familyvault.models.forms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FormDataStringTouchedEntry : BaseFormDataEntry() {
    override var touched: Boolean = true
    var value: String by mutableStateOf("")
}