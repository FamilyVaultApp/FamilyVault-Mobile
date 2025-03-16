package com.github.familyvault.ui.screens.start.createFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.FamilyGroupNameForm
import com.github.familyvault.forms.FamilyGroupNewMemberForm
import com.github.familyvault.ui.components.HeaderWithIcon
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.newFamilyMember.NewFamilyMemberFormContent
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.family_group_create_screen_title
import familyvault.composeapp.generated.resources.text_field_group_name_label
import org.jetbrains.compose.resources.stringResource

class FamilyGroupCreateMemberAndNameScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val newFamilyMemberForm = FamilyGroupNewMemberForm()
        val familyGroupNameForm = FamilyGroupNameForm()

        StartScreenScaffold {
            CreateFamilyGroupHeader()


            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                FamilyGroupCreateForm(newFamilyMemberForm, familyGroupNameForm)
                InitialScreenButton(
                    text = stringResource(Res.string.create_new_family_group_title),
                    enabled = newFamilyMemberForm.isFormValid() && familyGroupNameForm.isFormValid()
                ) {
                    navigator.replaceAll(
                        FamilyGroupCreateAssignPrivateKeyPasswordScreen(
                            newFamilyMemberForm.formData,
                            familyGroupNameForm.formData,
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun CreateFamilyGroupHeader() {
        HeaderWithIcon(
            stringResource(Res.string.family_group_create_screen_title),
            Icons.Filled.Groups
        )
    }

    @Composable
    private fun FamilyGroupCreateForm(
        newFamilyMemberForm: FamilyGroupNewMemberForm,
        familyGroupNameForm: FamilyGroupNameForm,
        isFormEnabled: Boolean = true,
    ) {
        Column {
            NewFamilyMemberFormContent(newFamilyMemberForm, isFormEnabled)
            TextField(
                value = familyGroupNameForm.familyGroupName,
                label = stringResource(Res.string.text_field_group_name_label),
                onValueChange = { familyGroupNameForm.setFamilyGroupName(it) },
                enabled = isFormEnabled,
                supportingText = { ValidationErrorMessage(familyGroupNameForm.familyGroupNameValidationError) }
            )
        }
    }
}