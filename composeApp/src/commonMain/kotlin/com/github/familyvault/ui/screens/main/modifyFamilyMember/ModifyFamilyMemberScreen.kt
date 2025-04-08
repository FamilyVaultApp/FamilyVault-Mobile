package com.github.familyvault.ui.screens.main.modifyFamilyMember

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.user_modification_choose_permission_content
import familyvault.composeapp.generated.resources.user_modification_guardian
import familyvault.composeapp.generated.resources.user_modification_guest
import familyvault.composeapp.generated.resources.user_modification_member
import familyvault.composeapp.generated.resources.user_modification_save_button
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class ModifyFamilyMemberScreen(val userId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val options = listOf(
            stringResource(Res.string.user_modification_guest),
            stringResource(Res.string.user_modification_member),
            stringResource(Res.string.user_modification_guardian)
        )
        var selectedIndex by remember { mutableStateOf(0) }
        var savingChanges by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val familyGroupService = koinInject<IFamilyGroupService>()

        val segmentedButtonColors = SegmentedButtonDefaults.colors().copy(
            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
            activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    userId,
                    showManagementButton = false
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
                    .padding(horizontal = AdditionalTheme.spacings.screenPadding)
            ) {
                Paragraph(stringResource(Res.string.user_modification_choose_permission_content))
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex,
                            label = { Paragraph(label) },
                            colors = segmentedButtonColors
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                InitialScreenButton(
                    text = stringResource(Res.string.user_modification_save_button),
                    enabled = !savingChanges,
                    onClick = {
                        coroutineScope.launch {
                            savingChanges = true
                            when (selectedIndex) {
                                0 -> {
                                    familyGroupService.changeFamilyMemberPermissionGroupToGuest(
                                        userId
                                    )
                                }

                                1 -> {
                                    familyGroupService.changeFamilyMemberPermissionGroupToMember(
                                        userId
                                    )
                                }

                                2 -> {
                                    familyGroupService.changeFamilyMemberPermissionGroupToGuardian(
                                        userId
                                    )
                                }
                            }
                            savingChanges = false
                            navigator.pop()
                        }
                    }
                )
            }
        }
    }
}