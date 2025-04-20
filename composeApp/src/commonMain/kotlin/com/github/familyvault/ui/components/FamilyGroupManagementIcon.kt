package com.github.familyvault.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.ui.screens.main.familyGroupSettings.FamilyGroupSettingsScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyGroupManagementIcon() {
    val navigator = LocalNavigator.currentOrThrow

    return IconButton(
        onClick = { navigator.parent?.push(FamilyGroupSettingsScreen()) }
    ) {
        Icon(
            Icons.Filled.Settings,
            stringResource(Res.string.settings_title)
        )
    }
}