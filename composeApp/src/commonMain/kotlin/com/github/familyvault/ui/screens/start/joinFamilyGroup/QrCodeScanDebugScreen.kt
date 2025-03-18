package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
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
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.screens.main.MainScreen
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

class QrCodeScanDebugScreen(val scanResult: String): Screen {

    @Composable
    override fun Content() {
        val familyGroupService = koinInject<IFamilyGroupService>()
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupNewMemberData: NewFamilyMemberData = Json.decodeFromString(scanResult)
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val contextId = familyGroupSessionService.getContextId()
        println(familyGroupNewMemberData.joinStatus?.token)
        LaunchedEffect(Unit) {
            familyGroupService.addMemberToFamilyGroup(contextId, familyGroupNewMemberData.firstname + " " + familyGroupNewMemberData.surname, familyGroupNewMemberData.keyPair.publicKey)
            delay(100)
            familyGroupService.updateTokenInfo(familyGroupNewMemberData.joinStatus?.token!!, contextId)
            delay(100)
            familyGroupService.updateTokenStatus(familyGroupNewMemberData.joinStatus?.token!!, 1)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Scanned message")
            Text(
                familyGroupNewMemberData.firstname + " " + familyGroupNewMemberData.surname
            )
            InitialScreenButton(
                onClick = {
                    navigator.replaceAll(MainScreen())
                }
            )
        }
    }
}