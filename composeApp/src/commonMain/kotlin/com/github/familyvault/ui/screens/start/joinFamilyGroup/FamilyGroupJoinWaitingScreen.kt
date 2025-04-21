package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyMemberAdditionService
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.screens.main.MainScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupJoinWaitingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyMemberAdditionService = koinInject<IFamilyMemberAdditionService>()
        val joinTokenService = koinInject<IJoinStatusService>()
        val joinFamilyGroupPayloadState = koinInject<IJoinFamilyGroupPayloadState>()
        val payload = remember { joinFamilyGroupPayloadState.getPayload() }
        val (firstname, surname, keyPair) = payload.newMemberData

        LaunchedEffect(Unit) {
            val joinStatusInfo = joinTokenService.waitAndGetJoinStatusInfo(payload.joinStatusToken)

            if (joinStatusInfo.contextId == null) {
                throw Exception("contextId from joinStatusInfo is null")
            }

            familyGroupService.joinFamilyGroupAndAssign(
                firstname,
                surname,
                joinFamilyGroupPayloadState.getEncryptedPrivateKey(),
                keyPair,
                joinStatusInfo.contextId
            )
            familyMemberAdditionService.afterJoinedToFamilyMembersOperations()

            navigator.replaceAll(MainScreen())
        }


        StartScreenScaffold {
            LoaderWithText(
                stringResource(Res.string.loading), modifier = Modifier.fillMaxSize()
            )
        }
    }
}
