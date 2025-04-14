package com.github.familyvault.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.ui.screens.main.FamilyGroupManagementScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.family_group_management_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyGroupManagementIcon(nextScreen: Screen) {
    val navigator = LocalNavigator.currentOrThrow

    return IconButton(
        onClick = {
            if (navigator.parent != null) {
                navigator.parent?.push(nextScreen)
            } else {
                navigator.push(nextScreen)
            }
        }
    ) {
        Icon(
            Icons.Filled.Settings,
            stringResource(Res.string.family_group_management_title)
        )
    }
}