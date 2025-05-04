package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.forms.TaskForm
import com.github.familyvault.states.IFamilyMembersState
import com.github.familyvault.ui.components.FamilyMemberPicker
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.assigned_person
import familyvault.composeapp.generated.resources.description
import familyvault.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun TaskFormContent(form: TaskForm) {
    val familyMembersState = koinInject<IFamilyMembersState>()

    Column {
        TextField(
            label = stringResource(Res.string.title),
            value = form.title,
            supportingText = {
                ValidationErrorMessage(form.titleValidationError)
            },
            onValueChange = { form.setTitle(it) }
        )

        TextField(
            label = stringResource(Res.string.description),
            value = form.description,
            supportingText = {
                ValidationErrorMessage(form.descriptionValidationError)
            },
            onValueChange = { form.setDescription(it) }
        )

        FamilyMemberPicker(
            label = stringResource(Res.string.assigned_person),
            familyMembers = familyMembersState.getAllFamilyMembers(),
            supportingText = {
                ValidationErrorMessage(form.pubKeyOfAssignedMemberValidationError)
            },
            selectedPubKey = form.pubKeyOfAssignedMember,
            onPick = {
                form.setPubKeyOfAssignedMember(it?.publicKey)
            }
        )
    }
}