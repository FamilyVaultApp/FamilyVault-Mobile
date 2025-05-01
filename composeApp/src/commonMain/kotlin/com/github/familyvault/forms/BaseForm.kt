package com.github.familyvault.forms

import com.github.familyvault.forms.models.BaseFormDataEntry

abstract class BaseForm {
    abstract fun validateForm()

    abstract fun isFormValid(): Boolean

    protected fun afterEntryUpdate(entry: BaseFormDataEntry) {
        entry.touched = true
        validateForm()
    }
}