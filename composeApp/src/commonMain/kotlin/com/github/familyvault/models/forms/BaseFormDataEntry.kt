package com.github.familyvault.models.forms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.familyvault.forms.validator.FormValidatorError

abstract class BaseFormDataEntry {
    var touched: Boolean by mutableStateOf(false)
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