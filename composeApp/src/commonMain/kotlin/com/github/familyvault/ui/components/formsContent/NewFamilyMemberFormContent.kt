package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.forms.FamilyGroupNewMemberForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewFamilyMemberFormContent(form: FamilyGroupNewMemberForm, enabled: Boolean = true) {
    Column {
        TextField(
            value = form.firstname,
            label = stringResource(Res.string.text_field_name_label),
            onValueChange = {
                form.setFirstname(it)
            },
            enabled = enabled,
            supportingText = { ValidationErrorMessage(form.firstnameValidationError) }
        )
        TextField(
            value = form.surname,
            label = stringResource(Res.string.text_field_surname_label),
            onValueChange = {
                form.setSurname(it)
            },
            enabled = enabled,
            supportingText = { ValidationErrorMessage(form.surnameValidationError) }
        )
    }
}