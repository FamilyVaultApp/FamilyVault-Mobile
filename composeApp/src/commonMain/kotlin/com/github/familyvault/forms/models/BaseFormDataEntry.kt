package com.github.familyvault.forms.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.familyvault.forms.validator.FormValidatorError

abstract class BaseFormDataEntry {
    open var touched: Boolean by mutableStateOf(false)
    var validationError: FormValidatorError? by mutableStateOf(null)

    fun getValidationErrorIfTouched(): FormValidatorError? {
        if (touched) {
            return validationError
        }
        return null
    }

    fun isValid(): Boolean {
        return validationError == null && touched
    }
}