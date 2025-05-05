package com.github.familyvault.forms.models

import androidx.compose.runtime.mutableStateListOf

class FormDataListEntry<T>: BaseFormDataEntry() {
    var list: MutableList<T> = mutableStateListOf()
}