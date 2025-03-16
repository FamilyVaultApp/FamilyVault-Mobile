package com.github.familyvault.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
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
import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.components.CustomIcon
import com.github.familyvault.components.InfoBox
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.ValidationErrorMessage
import com.github.familyvault.components.dialogs.FamilyGroupCreatingDialog
import com.github.familyvault.components.overrides.TextField
import com.github.familyvault.components.screen.StartScreenScaffold
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.forms.FamilyGroupCreateFormData
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.models.SelectedFamilyGroupAction
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.password_label
import familyvault.composeapp.generated.resources.private_key_about_content
import familyvault.composeapp.generated.resources.private_key_about_title
import familyvault.composeapp.generated.resources.private_key_assign_password_title
import familyvault.composeapp.generated.resources.repeat_password_label
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class PrivateKeyPasswordAssignScreen(private val familyGroupDraft: FamilyGroupCreateFormData, private val selectedFamilyGroupAction: SelectedFamilyGroupAction) :
    Screen {

    @Composable
    override fun Content() {
        val familyGroupService = koinInject<IFamilyGroupService>()
        val privMxClient = koinInject<IPrivMxClient>()
        val navigator = LocalNavigator.currentOrThrow
        val form by remember { mutableStateOf(PrivateKeyAssignPasswordForm()) }

        val coroutineScope = rememberCoroutineScope()
        var isCreatingFamilyGroup by remember { mutableStateOf(false) }

        StartScreenScaffold {
            if (isCreatingFamilyGroup) {
                FamilyGroupCreatingDialog()
            }
            PrivateKeyAssignPasswordHeader()
            CustomIcon(
                icon = Icons.Outlined.Key
            )
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.large))
            InfoBox(
                title = stringResource(Res.string.private_key_about_title),
                content = stringResource(Res.string.private_key_about_content)
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                PrivateKeyAssignForm(form)
                InitialScreenButton(
                    enabled = form.isFormValid()
                ) {
                    if (selectedFamilyGroupAction == SelectedFamilyGroupAction.Create) {
                        isCreatingFamilyGroup = true
                        coroutineScope.launch {
                            familyGroupService.createFamilyGroupAndAssign(
                                familyGroupDraft.firstname.value,
                                familyGroupDraft.surname.value,
                                form.password,
                                familyGroupDraft.familyGroupName.value,
                                "Description"
                            )
                            isCreatingFamilyGroup = false
                            navigator.replaceAll(DebugScreenContextId())
                        }
                    } else {
                        val keyPair = privMxClient.generatePairOfPrivateAndPublicKey(
                            form.password,
                            AppConfig.SALT
                        )
                        val newFamilyMemberData = NewFamilyMemberData(
                            firstname = familyGroupDraft.firstname.value,
                            surname = familyGroupDraft.surname.value,
                            keyPair = keyPair
                        )
                        navigator.replaceAll(
                            FamilyGroupNFCJoin(
                                Json.encodeToString(
                                    newFamilyMemberData
                                )
                            )
                        )
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
        Column {
            TextField(
                value = form.password,
                label = stringResource(Res.string.password_label),
                isPassword = true,
                onValueChange = { form.setPassword(it) },
                supportingText = { ValidationErrorMessage(form.passwordValidationError) }

            )
            TextField(
                value = form.repeatPassword,
                label = stringResource(Res.string.repeat_password_label),
                isPassword = true,
                onValueChange = { form.setRepeatPassword(it) },
                supportingText = { ValidationErrorMessage(form.passwordRepeatValidationError) }
            )
        }
    }
}