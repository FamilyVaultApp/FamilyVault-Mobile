package com.github.familyvault.ui.screens.main.addFamilyMember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.screens.main.FamilyGroupManagementScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class AddMemberToFamilyGroupBackendOperationsScreen(private val payload: AddFamilyMemberDataPayload) :
    Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val joinTokenService = koinInject<IJoinStatusService>()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            val (newMemberData, joinStatusToken) = payload

            coroutineScope.launch {
                joinTokenService.changeStateToPending(
                    joinStatusToken
                )
                val contextId = familyGroupSessionService.getContextId()
                familyGroupService.addMemberToFamilyGroup(
                    contextId, newMemberData.fullname, newMemberData.keyPair.publicKey
                )
                joinTokenService.changeStateToSuccess(
                    joinStatusToken, contextId
                )
                navigator.replaceAll(FamilyGroupManagementScreen())
            }
        }

        StartScreenScaffold {
            LoaderWithText(stringResource(Res.string.loading))
        }
    }
}