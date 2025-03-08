package com.github.familyvault.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.CustomIcon
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.ValidationErrorMessage
import com.github.familyvault.components.overrides.TextField
import com.github.familyvault.components.screen.StartScreenScaffold
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.forms.FamilyGroupCreateForm
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.family_group_create_screen_title
import familyvault.composeapp.generated.resources.text_field_group_name_label
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import org.jetbrains.compose.resources.stringResource

class FamilyGroupCreateScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val form = FamilyGroupCreateForm()

        StartScreenScaffold {
            CreateFamilyGroupHeader()
            CustomIcon(
                icon = Icons.Filled.Groups
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                FamilyGroupCreateForm(form)
                InitialScreenButton(
                    text = stringResource(Res.string.create_new_family_group_title),
                    enabled = form.isFormValid()
                ) {
                    navigator.replaceAll(PrivateKeyAssignPasswordScreen(form.formData))
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
        form: FamilyGroupCreateForm,
        isFormEnabled: Boolean = true,
    ) {
        Column{
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = form.firstname,
                label = { Paragraph(stringResource(Res.string.text_field_name_label)) },
                onValueChange = { form.setFirstname(it) },
                enabled = isFormEnabled,
                supportingText = { ValidationErrorMessage(form.firstnameValidationError) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = form.surname,
                label = { Paragraph(stringResource(Res.string.text_field_surname_label)) },
                onValueChange = { form.setSurname(it) },
                enabled = isFormEnabled,
                supportingText = { ValidationErrorMessage(form.surnameValidationError) }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = form.familyGroupName,
                label = { Paragraph(stringResource(Res.string.text_field_group_name_label)) },
                onValueChange = { form.setFamilyGroupName(it) },
                enabled = isFormEnabled,
                supportingText = { ValidationErrorMessage(form.familyGroupNameValidationError) }
            )
        }
    }
}