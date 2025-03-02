package com.github.familyvault.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.components.overrides.TopAppBar
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.family_group_management_title
import org.jetbrains.compose.resources.stringResource

class FamilyGroupManagementScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopAppBar(
                    stringResource(Res.string.family_group_management_title),
                    showManagementButton = false
                )
            },
            content = {}
        )
    }
}