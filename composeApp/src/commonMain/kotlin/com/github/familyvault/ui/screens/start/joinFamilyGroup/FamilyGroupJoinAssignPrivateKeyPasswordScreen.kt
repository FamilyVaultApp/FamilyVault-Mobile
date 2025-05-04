package com.github.familyvault.ui.screens.start.joinFamilyGroup

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
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.forms.FamilyMemberFormData
import com.github.familyvault.forms.PrivateKeyPasswordForm
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.models.enums.FormSubmitState
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.ui.components.BottomNextButton
import com.github.familyvault.ui.components.dialogs.CircularProgressIndicatorDialog
import com.github.familyvault.ui.components.dialogs.ErrorDialog
import com.github.familyvault.ui.components.formsContent.PrivateKeyFormContent
import com.github.familyvault.ui.components.privateKey.PrivateKeyAssignPasswordHeader
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.preparing_to_join_family_group_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupJoinAssignPrivateKeyPasswordScreen(private val newFamilyMemberDraft: FamilyMemberFormData) :
    Screen {
    @Composable
    override fun Content() {
        val joinFamilyGroupPayloadState = koinInject<IJoinFamilyGroupPayloadState>()
        val joinStatusService = koinInject<IJoinStatusService>()
        val privMxClient = koinInject<IPrivMxClient>()
        val coroutineScope = rememberCoroutineScope()
        val form by remember { mutableStateOf(PrivateKeyPasswordForm()) }
        val navigator = LocalNavigator.currentOrThrow
        var preparingToJoinFamilyGroupState by remember { mutableStateOf(FormSubmitState.IDLE) }

        StartScreenScaffold {
            when (preparingToJoinFamilyGroupState) {
                FormSubmitState.PENDING -> CircularProgressIndicatorDialog(
                    stringResource(Res.string.preparing_to_join_family_group_label)
                )

                FormSubmitState.ERROR -> ErrorDialog {
                    preparingToJoinFamilyGroupState = FormSubmitState.IDLE
                }

                else -> Unit
            }
            PrivateKeyAssignPasswordHeader()
            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom
            ) {
                PrivateKeyFormContent(
                    form,
                )
                BottomNextButton(
                    enabled = form.isFormValid(),
                ) {
                    coroutineScope.launch {
                        preparingToJoinFamilyGroupState = FormSubmitState.PENDING
                        try {
                            val keyPair = privMxClient.generatePairOfPrivateAndPublicKey(
                                form.password
                            )
                            val encryptedPassword =
                                privMxClient.encryptPrivateKeyPassword(form.password)
                            val joinStatus = joinStatusService.generateJoinStatus()

                            val newFamilyMemberData = NewFamilyMemberData(
                                firstname = newFamilyMemberDraft.firstname.value,
                                surname = newFamilyMemberDraft.surname.value,
                                keyPair = keyPair
                            )
                            joinFamilyGroupPayloadState.update(
                                joinStatus.token, newFamilyMemberData, encryptedPassword
                            )
                            navigator.replaceAll(FamilyGroupJoinNfc())
                            preparingToJoinFamilyGroupState = FormSubmitState.IDLE
                        } catch (e: Exception) {
                            preparingToJoinFamilyGroupState = FormSubmitState.ERROR
                            println(e)
                        }
                    }
                }
            }
        }
    }
}