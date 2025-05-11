package com.github.familyvault.ui.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.models.SelfHostedConnectionInfo
import com.github.familyvault.ui.components.AppIconAndName
import com.github.familyvault.ui.components.BottomNextButton
import com.github.familyvault.ui.components.OptionButton
import com.github.familyvault.ui.components.OptionButtonType
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.screens.start.createFamilyGroup.FamilyGroupCreateMemberAndNameScreen
import com.github.familyvault.ui.screens.start.joinFamilyGroup.FamilyGroupJoinNewMemberScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_new_family_group_content
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.join_existing_family_group_content
import familyvault.composeapp.generated.resources.join_existing_family_group_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupCreateOrJoinScreen() : Screen {
    private enum class FamilyGroupAction {
        Join,
        Create
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedAction by remember { mutableStateOf<FamilyGroupAction?>(null) }
        val familyVaultBackendClient = koinInject<IFamilyVaultBackendClient>()

        LaunchedEffect(Unit) {
            println(familyVaultBackendClient.getCustomBackendUrl())
        }

        StartScreenScaffold {
            AppIconAndName()
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                OptionButtons(
                    selectedAction = selectedAction,
                    onActionSelected = { selectedAction = it }
                )
                BottomNextButton(
                    enabled = selectedAction != null,
                    onClick = {
                        if (selectedAction == FamilyGroupAction.Join) {
                            navigator.push(FamilyGroupJoinNewMemberScreen())
                        } else {
                            navigator.push(FamilyGroupCreateMemberAndNameScreen())
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun OptionButtons(
        selectedAction: FamilyGroupAction?,
        onActionSelected: (FamilyGroupAction) -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            OptionButton(
                title = stringResource(Res.string.join_existing_family_group_title),
                content = stringResource(Res.string.join_existing_family_group_content),
                icon = Icons.AutoMirrored.Outlined.Login,
                type = OptionButtonType.First,
                isSelected = selectedAction == FamilyGroupAction.Join,
                onClick = { onActionSelected(FamilyGroupAction.Join) }
            )
            OptionButton(
                title = stringResource(Res.string.create_new_family_group_title),
                content = stringResource(Res.string.create_new_family_group_content),
                icon = Icons.Outlined.GroupAdd,
                type = OptionButtonType.Second,
                isSelected = selectedAction == FamilyGroupAction.Create,
                onClick = { onActionSelected(FamilyGroupAction.Create) }
            )
        }
    }
}