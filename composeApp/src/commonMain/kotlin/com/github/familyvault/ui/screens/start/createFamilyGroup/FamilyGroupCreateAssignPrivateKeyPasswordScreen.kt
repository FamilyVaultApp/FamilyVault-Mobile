package com.github.familyvault.ui.screens.start.createFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.FamilyGroupNameFormData
import com.github.familyvault.forms.FamilyMemberNewMemberFormData
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.models.enums.FormSubmitState
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyMemberAdditionService
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.components.dialogs.CircularProgressIndicatorDialog
import com.github.familyvault.ui.components.dialogs.ErrorDialog
import com.github.familyvault.ui.components.formsContent.AssignPrivateKeyFormContent
import com.github.familyvault.ui.components.privateKey.PrivateKeyAssignPasswordHeader
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.creating_family_group_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupCreateAssignPrivateKeyPasswordScreen(
    private val familyGroupDraft: FamilyMemberNewMemberFormData,
    private val familyGroupNameDraft: FamilyGroupNameFormData
) : Screen {
    @Composable
    override fun Content() {
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyMemberAdditionService = koinInject<IFamilyMemberAdditionService>()
        val navigator = LocalNavigator.currentOrThrow
        val form by remember { mutableStateOf(PrivateKeyAssignPasswordForm()) }

        val coroutineScope = rememberCoroutineScope()
        var createFamilyGroupState by remember { mutableStateOf(FormSubmitState.IDLE) }

        StartScreenScaffold {
            when (createFamilyGroupState) {
                FormSubmitState.PENDING -> CircularProgressIndicatorDialog(
                    stringResource(Res.string.creating_family_group_label)
                )

                FormSubmitState.ERROR -> ErrorDialog {
                    createFamilyGroupState = FormSubmitState.IDLE
                }

                else -> Unit
            }
            PrivateKeyAssignPasswordHeader()
            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom
            ) {
                AssignPrivateKeyFormContent(
                    form,
                )
                InitialScreenButton(
                    enabled = form.isFormValid(),
                ) {
                    coroutineScope.launch {
                        createFamilyGroupState = FormSubmitState.PENDING
                        try {
                            familyGroupService.createFamilyGroupAndAssign(
                                familyGroupDraft.firstname.value,
                                familyGroupDraft.surname.value,
                                form.password,
                                familyGroupNameDraft.familyGroupName.value,
                                "Description"
                            )
                            familyMemberAdditionService.afterJoinedToFamilyMembersOperations()

                            navigator.replaceAll(DebugScreenContextId())
                            createFamilyGroupState = FormSubmitState.IDLE
                        } catch (e: Exception) {
                            createFamilyGroupState = FormSubmitState.ERROR
                            println(e)
                        }
                    }
                }
            }
        }
    }
}