package com.github.familyvault.forms.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class FormDataNullableString : BaseFormDataEntry() {
    var value: String? by mutableStateOf(null)
}