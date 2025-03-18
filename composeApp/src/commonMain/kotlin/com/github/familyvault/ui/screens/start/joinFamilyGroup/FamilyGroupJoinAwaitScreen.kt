package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.start.createFamilyGroup.DebugScreenContextId
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class FamilyGroupJoinAwaitScreen(private val userData: NewFamilyMemberData, private var joinStatus: FamilyMemberJoinStatus, private val token: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var currentJoinStatus by remember { mutableStateOf(joinStatus) }
        var joinProcessComplete by remember { mutableStateOf(false) }
        val familyGroupService = koinInject<IFamilyGroupService>()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(token) {
            currentJoinStatus = familyGroupService.getTokenStatus(token)
            while (currentJoinStatus.status == JoinTokenStatus.Pending) {
                delay(1000)
                currentJoinStatus = familyGroupService.getTokenStatus(token)
                println("Received joinStatus: " + currentJoinStatus.status)
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (currentJoinStatus.status == JoinTokenStatus.Pending) {
                Row {
                    CircularProgressIndicator()
                    Paragraph("\nOczekiwanie...")
                }
            } else if (currentJoinStatus.status == JoinTokenStatus.Success) {
                if (currentJoinStatus.info != null)
                {
                    coroutineScope.launch {
                        familyGroupService.joinFamilyGroupAndAssign(
                            userData.firstname,
                            userData.surname,
                            userData.keyPair,
                            currentJoinStatus.info!!.contextId
                        )
                        joinProcessComplete = true
                    }
                    if (joinProcessComplete)
                    {
                        navigator.replaceAll(DebugScreenContextId())
                    }
                }
            }
        }
    }
}
