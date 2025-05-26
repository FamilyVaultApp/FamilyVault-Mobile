package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.forms.SelfHostingAddressForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField

@Composable
fun SelfHostingAddressFormContent(form: SelfHostingAddressForm, onFormChange: () -> Unit) {
    Column {
        TextField(
            value = form.address,
            label = "URL serwera",
            onValueChange = {
                form.setAddress(it)
                onFormChange()
            },
            supportingText = { ValidationErrorMessage(form.addressValidationError) }
        )
    }
}