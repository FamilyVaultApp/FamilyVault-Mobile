package com.github.familyvault.ui.components.overrides

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.ui.components.FamilyGroupManagementIcon
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.main.FamilyGroupManagementScreen
import androidx.compose.material3.TopAppBar as MdTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    showManagementButton: Boolean = true,
    managementButtonNextScreen: Screen = FamilyGroupManagementScreen()
) {
    return MdTopAppBar(
        title = { Paragraph(title) },
        actions = {
            if (showManagementButton)
                FamilyGroupManagementIcon(managementButtonNextScreen)
        }
    )
}