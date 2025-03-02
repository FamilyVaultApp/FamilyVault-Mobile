package com.github.familyvault.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.screens.FamilyGroupManagementScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.family_group_management_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyGroupManagementIcon() {
    val navigator = LocalNavigator.currentOrThrow

    return IconButton(
        content = {
            Icon(
                Icons.Filled.Settings,
                stringResource(Res.string.family_group_management_title)
            )
        },
        onClick = { navigator.parent?.push(FamilyGroupManagementScreen()) }
    )
}