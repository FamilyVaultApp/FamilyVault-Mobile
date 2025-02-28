package com.github.familyvault.screens

import CustomIcon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import com.github.familyvault.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.AppConfig
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.TextField
import com.github.familyvault.components.dialogs.FamilyGroupCreatingDialog
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.models.formData.FamilyGroupFormData
import com.github.familyvault.services.IFamilyGroupManagerService
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.app_icon_alt
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.family_group_create_screen_title
import familyvault.composeapp.generated.resources.initial_form_empty_error
import familyvault.composeapp.generated.resources.initial_form_overfill_error
import familyvault.composeapp.generated.resources.text_field_group_name_label
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupCreateScreen : Screen {

    @Composable
    override fun Content() {
        val familyGroupManager = koinInject<IFamilyGroupManagerService>()
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()

        var formData by remember { mutableStateOf(FamilyGroupFormData()) }
        var isCreatingFamilyGroup by remember { mutableStateOf(false) }

        StartScreen {
            if (isCreatingFamilyGroup) {
                FamilyGroupCreatingDialog()
            }
            CreateFamilyGroupHeader()
            CustomIcon(
                icon = Icons.Filled.Group
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                FamilyGroupCreateForm(formData, !isCreatingFamilyGroup) {
                    formData = it
                }
                InitialScreenButton(
                    text = stringResource(Res.string.create_new_family_group_title),
                    enabled = formData.formIsCorrect
                ) {
                    isCreatingFamilyGroup = true
                    coroutineScope.launch {
                        familyGroupManager.createFamilyGroup(formData.firstname.value, formData.lastname.value, "sec", formData.familyGroupName.value, "...")
                        isCreatingFamilyGroup = false
                        navigator.replaceAll(DebugScreenContextId())
                    }
                }
            }
        }
    }

    @Composable
    private fun CreateFamilyGroupHeader() {
        return Box(
            modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)
        ) {
            Headline1(
                stringResource(Res.string.family_group_create_screen_title),
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    private fun FamilyGroupCreateForm(
        formData: FamilyGroupFormData,
        isFormEnabled: Boolean = true,
        onFormChange: (formData: FamilyGroupFormData) -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = formData.firstname.value,
                label = { Paragraph(stringResource(Res.string.text_field_name_label)) },
                onValueChange = { newValue ->
                        val validatedForm = updateForm(formData, "firstname", newValue)
                        onFormChange(validatedForm)
                },
                enabled = isFormEnabled,
                supportingText = { ValidationErrorMessage(formData.firstname.validationError) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = formData.lastname.value,
                label = { Paragraph(stringResource(Res.string.text_field_surname_label)) },
                onValueChange = { newValue ->
                    val validatedForm = updateForm(formData, "lastname", newValue)
                        onFormChange(validatedForm)
                },
                enabled = isFormEnabled,
                supportingText = { ValidationErrorMessage(formData.lastname.validationError) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = formData.familyGroupName.value,
                label = { Paragraph(stringResource(Res.string.text_field_group_name_label)) },
                onValueChange = { newValue ->
                    val validatedForm = updateForm(formData, "familyGroupName", newValue)
                    onFormChange(validatedForm)
                },
                enabled = isFormEnabled,
                supportingText = { ValidationErrorMessage(formData.familyGroupName.validationError) }
            )
        }
    }

    @Composable
    fun ValidationErrorMessage(error: String?) {
        if (!error.isNullOrBlank()) {
            val errorMessage = when (error) {
                "Empty" -> stringResource(Res.string.initial_form_empty_error, AppConfig.MAX_NAME_INPUT_LENGTH)
                "Overfill" -> stringResource(Res.string.initial_form_overfill_error, AppConfig.MAX_NAME_INPUT_LENGTH)
                else -> ""
            }
            if (errorMessage.isNotEmpty()) {
                Paragraph(text = errorMessage, color = Color.Red)
            }
        }
    }

    private fun updateForm(currentFormState: FamilyGroupFormData, editedFieldName: String, newValue: String): FamilyGroupFormData
    {
         val updatedForm = when(editedFieldName) {
             "firstname" -> FamilyGroupFormData(
                 firstname = if (newValue.length <= 64) currentFormState.firstname.copy(
                     value = newValue
                 ) else currentFormState.firstname,
                 lastname = currentFormState.lastname,
                 familyGroupName = currentFormState.familyGroupName,
                 formIsCorrect = currentFormState.formIsCorrect,
                 editedFields = currentFormState.editedFields
             )

             "lastname" -> FamilyGroupFormData(
                 firstname = currentFormState.firstname,
                 lastname = if (newValue.length <= 64) currentFormState.lastname.copy(
                     value = newValue
                 ) else currentFormState.lastname,
                 familyGroupName = currentFormState.familyGroupName,
                 formIsCorrect = currentFormState.formIsCorrect,
                 editedFields = currentFormState.editedFields
             )

             "familyGroupName" ->  FamilyGroupFormData(
                 firstname = currentFormState.firstname,
                 lastname = currentFormState.lastname,
                 familyGroupName = if (newValue.length <= 64) currentFormState.familyGroupName.copy(
                     value = newValue
                 ) else currentFormState.familyGroupName,
                 formIsCorrect = currentFormState.formIsCorrect,
                 editedFields = currentFormState.editedFields
             )

             else -> currentFormState
         }
        updatedForm.editedFields.add(editedFieldName)
        val validatedForm = updatedForm.validateForm()
        return validatedForm
    }

}