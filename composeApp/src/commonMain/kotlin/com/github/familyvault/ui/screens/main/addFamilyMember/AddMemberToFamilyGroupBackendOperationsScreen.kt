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
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.models.enums.JoinTokenStatus
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.typography.Paragraph
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

class AddMemberToFamilyGroupBackendOperationsScreen(val scanResult: String): Screen {

    @Composable
    override fun Content() {
        var joinProcessComplete by remember { mutableStateOf(false) }
        val familyGroupService = koinInject<IFamilyGroupService>()
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupNewMemberData: NewFamilyMemberData = Json.decodeFromString(scanResult)
        var currentJoinInformation by remember { mutableStateOf(FamilyMemberJoinStatus(familyGroupNewMemberData.joinToken, JoinTokenStatus.Pending, null)) }
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val contextId = familyGroupSessionService.getContextId()

        LaunchedEffect(Unit) {
            familyGroupService.addMemberToFamilyGroup(contextId, familyGroupNewMemberData.firstname + " " + familyGroupNewMemberData.surname, familyGroupNewMemberData.keyPair.publicKey)
            delay(100)
            familyGroupService.updateTokenInfo(currentJoinInformation.token, contextId)
            delay(100)
            familyGroupService.updateTokenStatus(currentJoinInformation.token, 1)
            joinProcessComplete = true
        }

        if (!joinProcessComplete) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    CircularProgressIndicator()
                    Paragraph("\nOczekiwanie...")
                }
            }
        } else {
            navigator.replaceAll(AddMemberToFamilyGroupScreen())
        }
    }
}