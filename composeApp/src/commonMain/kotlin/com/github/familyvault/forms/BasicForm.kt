package com.github.familyvault.forms

import com.github.familyvault.models.forms.BaseFormDataEntry

abstract class BasicForm {
    abstract fun validateForm()

    abstract fun isFormValid(): Boolean

    protected fun afterEntryUpdate(entry: BaseFormDataEntry) {
        entry.touched = true
        validateForm()
    }
}