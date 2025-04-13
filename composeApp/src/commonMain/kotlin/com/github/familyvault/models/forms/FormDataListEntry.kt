package com.github.familyvault.models.forms

import androidx.compose.runtime.mutableStateListOf

class FormDataListEntry<T>: BaseFormDataEntry() {
    var list: MutableList<T> = mutableStateListOf()
}