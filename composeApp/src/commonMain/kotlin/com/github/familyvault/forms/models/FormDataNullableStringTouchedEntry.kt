package com.github.familyvault.forms.models

class FormDataNullableStringTouchedEntry : FormDataNullableString() {
    override var touched: Boolean = true
}