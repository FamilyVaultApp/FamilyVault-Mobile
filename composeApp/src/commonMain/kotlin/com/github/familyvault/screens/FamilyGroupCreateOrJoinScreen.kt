package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.AppIconAndName
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.OptionButton
import com.github.familyvault.components.OptionButtonType
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_new_family_group_content
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.join_existing_family_group_content
import familyvault.composeapp.generated.resources.join_existing_family_group_title
import org.jetbrains.compose.resources.stringResource

class FamilyGroupCreateOrJoinScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedOption by remember { mutableStateOf<OptionButtonType?>(null) }

        StartScreen {
            AppIconAndName()
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                OptionButtons(
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it }
                )
                InitialScreenButton(
                    enabled = selectedOption != null,
                    onClick = {
                        navigator.push(FamilyGroupCreateScreen())
                    }
                )
            }
        }
    }

    @Composable
    private fun OptionButtons(
        selectedOption: OptionButtonType?,
        onOptionSelected: (OptionButtonType) -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            OptionButton(
                title = stringResource(Res.string.join_existing_family_group_title),
                content = stringResource(Res.string.join_existing_family_group_content),
                icon = Icons.AutoMirrored.Outlined.Login,
                type = OptionButtonType.First,
                isSelected = selectedOption == OptionButtonType.First,
                onClick = { onOptionSelected(OptionButtonType.First) }
            )
            OptionButton(
                title = stringResource(Res.string.create_new_family_group_title),
                content = stringResource(Res.string.create_new_family_group_content),
                icon = Icons.Outlined.GroupAdd,
                type = OptionButtonType.Second,
                isSelected = selectedOption == OptionButtonType.Second,
                onClick = { onOptionSelected(OptionButtonType.Second) }
            )
        }
    }
}