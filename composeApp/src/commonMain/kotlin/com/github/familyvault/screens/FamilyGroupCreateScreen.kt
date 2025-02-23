package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.TextField
import com.github.familyvault.components.dialogs.FamilyGroupCreatingDialog
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.models.formDatas.FamilyGroupFormData
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
            UserProfilePicture()
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
                        familyGroupManager.createFamilyGroup(formData.familyGroupName)
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
    private fun UserProfilePicture() {
        return Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.AccountCircle,
                stringResource(Res.string.app_icon_alt),
                modifier = Modifier.size(125.dp),
                tint = AdditionalTheme.colors.firstOptionPrimaryColor
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
                value = formData.firstname,
                label = { Paragraph(stringResource(Res.string.text_field_name_label)) },
                onValueChange = {
                    if (formData.firstname.length <= 64 || it.length < formData.firstname.length)
                    {
                        val updatedForm = formData.copy(firstname = it).validateForm()
                        updatedForm.editedFields.add("firstname")
                        onFormChange(updatedForm)
                    }
                     },
                enabled = isFormEnabled,
                supportingText = {
                    if (!formData.firstNameError.isNullOrBlank())
                    {
                        if (formData.firstNameError == "Empty")
                        {
                            Paragraph(text = stringResource(Res.string.initial_form_empty_error), color = Color.Red)
                        } else if (formData.firstNameError == "Overfill") {
                            Paragraph(text = stringResource(Res.string.initial_form_overfill_error), color = Color.Red)
                        }
                    }
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = formData.lastname,
                label = { Paragraph(stringResource(Res.string.text_field_surname_label)) },
                onValueChange = {
                    if (formData.lastname.length <= 64 || it.length < formData.lastname.length)
                    {
                        val updatedForm = formData.copy(lastname = it).validateForm()
                        updatedForm.editedFields.add("lastname")
                        onFormChange(updatedForm)
                    }
                                },
                enabled = isFormEnabled,
                supportingText = {
                    if (!formData.lastNameError.isNullOrBlank())
                    {
                        if (formData.lastNameError == "Empty")
                        {
                            Paragraph(text = stringResource(Res.string.initial_form_empty_error), color = Color.Red)
                        } else if (formData.lastNameError == "Overfill") {
                            Paragraph(text = stringResource(Res.string.initial_form_overfill_error), color = Color.Red)
                        }
                    }
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = formData.familyGroupName,
                label = { Paragraph(stringResource(Res.string.text_field_group_name_label)) },
                onValueChange = {
                    if (formData.familyGroupName.length <= 64 || it.length < formData.familyGroupName.length)
                    {
                        val updatedForm = formData.copy(familyGroupName = it).validateForm()
                        updatedForm.editedFields.add("familyGroupName")
                        onFormChange(updatedForm)
                    }
                                },
                enabled = isFormEnabled,
                supportingText = {
                    if (!formData.familyGroupNameError.isNullOrBlank())
                    {
                        if (formData.familyGroupNameError == "Empty")
                        {
                            Paragraph(stringResource(Res.string.initial_form_empty_error), color = Color.Red)
                        } else if (formData.familyGroupNameError == "Overfill") {
                            Paragraph(stringResource(Res.string.initial_form_overfill_error), color = Color.Red)
                        }
                    }
                }
            )
        }
    }
}