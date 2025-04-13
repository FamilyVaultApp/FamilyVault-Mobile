package com.github.familyvault.ui.components.formsContent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.github.familyvault.forms.FamilyGroupNameForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.text_field_group_name_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyGroupNameFormContent(form: FamilyGroupNameForm, enabled: Boolean = true) {

    var familyGroupName = rememberSaveable { mutableStateOf(form.familyGroupName) }

    LaunchedEffect(familyGroupName.value) {
        if (familyGroupName.value != form.familyGroupName) {
            form.setFamilyGroupName(familyGroupName.value)
        }
    }

    TextField(
        value = familyGroupName.value,
        label = stringResource(Res.string.text_field_group_name_label),
        onValueChange = {
            familyGroupName.value = it
            form.setFamilyGroupName(it)
        },
        enabled = enabled,
        supportingText = { ValidationErrorMessage(form.familyGroupNameValidationError) }
    )
}