package com.github.familyvault.ui.components.formsContent

import androidx.compose.runtime.Composable
import com.github.familyvault.forms.FamilyGroupNameForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.text_field_group_name_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyGroupNameFormContent(form: FamilyGroupNameForm, enabled: Boolean = true) {

    TextField(
        value = form.familyGroupName,
        label = stringResource(Res.string.text_field_group_name_label),
        onValueChange = {
            form.setFamilyGroupName(it)
        },
        enabled = enabled,
        supportingText = { ValidationErrorMessage(form.familyGroupNameValidationError) }
    )
}