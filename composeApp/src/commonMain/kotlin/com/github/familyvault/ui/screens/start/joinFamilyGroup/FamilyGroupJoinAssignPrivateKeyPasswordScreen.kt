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
import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.forms.FamilyMemberNewMemberFormData
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.models.FamilyMemberJoinStatus
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.components.privateKey.AssignPrivateKeyFormContent
import com.github.familyvault.ui.components.privateKey.PrivateKeyAssignPasswordHeader
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class FamilyGroupJoinAssignPrivateKeyPasswordScreen(private val newFamilyMemberDraft: FamilyMemberNewMemberFormData) :
    Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val form by remember { mutableStateOf(PrivateKeyAssignPasswordForm()) }
        val privMxClient = koinInject<IPrivMxClient>()
        StartScreenScaffold {
            PrivateKeyAssignPasswordHeader()
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                AssignPrivateKeyFormContent(
                    form,
                )
                InitialScreenButton(
                    enabled = form.isFormValid(),
                ) {
                    val keyPair = privMxClient.generatePairOfPrivateAndPublicKey(form.password, AppConfig.SALT)
                    val newFamilyMemberData = NewFamilyMemberData(
                        firstname = newFamilyMemberDraft.firstname.value,
                        surname = newFamilyMemberDraft.surname.value,
                        keyPair = keyPair,
                        joinToken = "notYetGenerated"
                    )
                    navigator.replaceAll(FamilyGroupJoinNfc(newFamilyMemberData))
                }
            }
        }
    }
}