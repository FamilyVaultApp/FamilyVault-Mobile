package com.github.familyvault.ui.screens.main.addFamilyMember

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyMemberJoinStatus
import com.github.familyvault.models.NewFamilyMemberDataPayload
import com.github.familyvault.models.enums.JoinTokenStatus
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IJoinTokenService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.typography.Paragraph
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

class AddMemberToFamilyGroupBackendOperationsScreen(private val scanResult: NewFamilyMemberDataPayload): Screen {

    @Composable
    override fun Content() {
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val joinTokenService = koinInject<IJoinTokenService>()
        val navigator = LocalNavigator.currentOrThrow
        var currentJoinInformation: FamilyMemberJoinStatus? by remember { mutableStateOf(null) }

        val contextId = familyGroupSessionService.getContextId()

        LaunchedEffect(Unit) {

            currentJoinInformation = joinTokenService.getJoinStatus(scanResult.joinStatus!!.token)
            if (currentJoinInformation!!.state != JoinTokenStatus.Success)
            {
                familyGroupService.addMemberToFamilyGroup(contextId, scanResult.newMemberData.fullname, scanResult.newMemberData.keyPair.publicKey)
                delay(100)
                joinTokenService.changeJoinStatusStateToAccept(currentJoinInformation!!.token, contextId)
                delay(100)
            }
        }
        if (currentJoinInformation == null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    LoaderWithText("Oczekiwanie...")
                }
            }
        } else if (currentJoinInformation!!.state != JoinTokenStatus.Success) {
            navigator.replaceAll(AddMemberToFamilyGroupScreen())
        }
    }
}