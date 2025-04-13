package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.github.familyvault.forms.FamilyGroupNewMemberForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewFamilyMemberFormContent(form: FamilyGroupNewMemberForm, enabled: Boolean = true) {

    val firstname = rememberSaveable { mutableStateOf(form.firstname) }
    val surname = rememberSaveable { mutableStateOf(form.surname) }

    LaunchedEffect(firstname.value) {
        if (firstname.value != form.firstname) {
            form.setFirstname(firstname.value)
        }
    }

    LaunchedEffect(surname.value) {
        if (surname.value != form.surname) {
            form.setSurname(surname.value)
        }
    }

    Column {
        TextField(
            value = firstname.value,
            label = stringResource(Res.string.text_field_name_label),
            onValueChange = { firstname.value = it },
            enabled = enabled,
            supportingText = { ValidationErrorMessage(form.firstnameValidationError) }
        )
        TextField(
            value = surname.value,
            label = stringResource(Res.string.text_field_surname_label),
            onValueChange = { surname.value = it },
            enabled = enabled,
            supportingText = { ValidationErrorMessage(form.surnameValidationError) }
        )
    }
}