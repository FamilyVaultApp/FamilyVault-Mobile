package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.forms.FamilyMemberNewMemberFormData
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.components.formsContent.AssignPrivateKeyFormContent
import com.github.familyvault.ui.components.privateKey.PrivateKeyAssignPasswordHeader
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class FamilyGroupJoinAssignPrivateKeyPasswordScreen(private val newFamilyMemberDraft: FamilyMemberNewMemberFormData) :
    Screen {
    @Composable
    override fun Content() {
        val joinFamilyGroupPayloadState = koinInject<IJoinFamilyGroupPayloadState>()
        val joinStatusService = koinInject<IJoinStatusService>()
        val privMxClient = koinInject<IPrivMxClient>()
        val coroutineScope = rememberCoroutineScope()
        val form by remember { mutableStateOf(PrivateKeyAssignPasswordForm()) }
        val navigator = LocalNavigator.currentOrThrow

        StartScreenScaffold {
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
                    }
                }
            }
        }
    }
}