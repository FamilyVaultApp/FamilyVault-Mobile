package com.github.familyvault.ui.screens.main.familyGroupSettings.familyGroupMember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IFamilyMemberAdditionService
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.screen.StartScreenScaffold
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
        val familyMemberAdditionService = koinInject<IFamilyMemberAdditionService>()
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

                familyMemberAdditionService.addMemberToFamilyGroup(contextId, newMemberData)

                joinTokenService.changeStateToSuccess(
                    joinStatusToken, contextId
                )
                navigator.popUntil { it is ModifyFamilyMemberScreen }
            }
        }

        StartScreenScaffold {
            LoaderWithText(stringResource(Res.string.loading))
        }
    }
}