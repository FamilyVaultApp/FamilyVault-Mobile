package com.github.familyvault.screens.initial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.InfoBox
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.ValidationErrorMessage
import com.github.familyvault.components.dialogs.FamilyGroupCreatingDialog
import com.github.familyvault.components.overrides.TextField
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.forms.FamilyGroupCreateFormData
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.services.IFamilyGroupManagerService
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.password_label
import familyvault.composeapp.generated.resources.private_key_about_content
import familyvault.composeapp.generated.resources.private_key_about_title
import familyvault.composeapp.generated.resources.private_key_assign_password_title
import familyvault.composeapp.generated.resources.repeat_password_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class PrivateKeyAssignPasswordScreen(val familyGroupDraft: FamilyGroupCreateFormData) :
    Screen {
    @Composable
    override fun Content() {
        val familyGroupManager = koinInject<IFamilyGroupManagerService>()
        val navigator = LocalNavigator.currentOrThrow
        val form by remember { mutableStateOf(PrivateKeyAssignPasswordForm()) }

        val coroutineScope = rememberCoroutineScope()
        var isCreatingFamilyGroup by remember { mutableStateOf(false) }

        StartScreen {
            if (isCreatingFamilyGroup) {
                FamilyGroupCreatingDialog()
            }
            PrivateKeyAssignPasswordHeader()
            InfoBox(
                title = stringResource(Res.string.private_key_about_title),
                content = stringResource(Res.string.private_key_about_content)
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
                ) {
                    PrivateKeyAssignForm(form)
                }
                InitialScreenButton(
                    enabled = form.isFormValid()
                ) {
                    isCreatingFamilyGroup = true
                    coroutineScope.launch {
                        familyGroupManager.createFamilyGroup(
                            familyGroupDraft.firstname.value,
                            familyGroupDraft.surname.value,
                            form.password,
                            familyGroupDraft.familyGroupName.value,
                            "Description"
                        )
                        isCreatingFamilyGroup = false
                        navigator.replaceAll(DebugScreenContextId())
                    }
                }
            }
        }
    }

    @Composable
    private fun PrivateKeyAssignPasswordHeader() {
        return Box(
            modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)
        ) {
            Headline1(
                stringResource(Res.string.private_key_assign_password_title),
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    private fun PrivateKeyAssignForm(form: PrivateKeyAssignPasswordForm) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = form.password,
            label = { Paragraph(stringResource(Res.string.password_label)) },
            isPassword = true,
            onValueChange = { form.setPassword(it) },
            supportingText = { ValidationErrorMessage(form.passwordValidationError) }

        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = form.repeatPassword,
            label = { Paragraph(stringResource(Res.string.repeat_password_label)) },
            isPassword = true,
            onValueChange = { form.setRepeatPassword(it) },
            supportingText = { ValidationErrorMessage(form.passwordRepeatValidationError) }
        )
    }
}