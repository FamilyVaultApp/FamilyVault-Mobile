package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.github.familyvault.AppConfig
import com.github.familyvault.models.FamilyMemberJoinStatus
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.models.NewFamilyMemberDataPayload
import com.github.familyvault.models.enums.JoinTokenStatus
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IJoinTokenService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.screens.start.createFamilyGroup.DebugScreenContextId
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

class FamilyGroupJoinAwaitScreen(private val userData: NewFamilyMemberDataPayload) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var currentJoinInformation by remember { mutableStateOf(FamilyMemberJoinStatus(userData.joinStatus!!.token, JoinTokenStatus.Pending, null)) }
        val familyGroupService = koinInject<IFamilyGroupService>()
        val joinTokenService = koinInject<IJoinTokenService>()

        LaunchedEffect(Unit) {
            do {
                currentJoinInformation = UpdateTokenStatus(joinTokenService)
            } while (currentJoinInformation.state == JoinTokenStatus.Pending)
            if (currentJoinInformation.info != null) {
                FamilyGroupJoinAndAssign(userData.newMemberData, currentJoinInformation, familyGroupService)
            }
        }
        StartScreenScaffold {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (currentJoinInformation.state == JoinTokenStatus.Pending) {
                    LoaderWithText("Oczekiwanie...")
                } else if (currentJoinInformation.state == JoinTokenStatus.Success) {
                        if (currentJoinInformation.state == JoinTokenStatus.Success) {
                            navigator.replaceAll(DebugScreenContextId())
                        }
                    }
            }
        }
    }

    suspend fun FamilyGroupJoinAndAssign(userData: NewFamilyMemberData, joinStatus: FamilyMemberJoinStatus, familyGroupService: IFamilyGroupService) {
        familyGroupService.joinFamilyGroupAndAssign(
            userData.firstname,
            userData.surname,
            userData.keyPair,
            joinStatus.info!!.contextId
        )
    }

    suspend fun UpdateTokenStatus(joinTokenService: IJoinTokenService): FamilyMemberJoinStatus {
        var joinStatus = joinTokenService.getJoinStatus(userData.joinStatus!!.token)
        delay(AppConfig.BACKEND_REQUEST_INTERVAL_LENGTH_MS)
        return joinStatus
    }
}
