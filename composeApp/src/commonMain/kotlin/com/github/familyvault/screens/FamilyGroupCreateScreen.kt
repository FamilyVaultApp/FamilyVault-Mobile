package com.github.familyvault.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.TextField
import com.github.familyvault.components.dialogs.FamilyGroupCreatingDialog
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.services.IFamilyGroupManagerService
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.app_icon_alt
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.family_group_create_screen_title
import familyvault.composeapp.generated.resources.text_field_group_name_label
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupCreateScreen : Screen {

    @Composable
    override fun Content() {
        val familyGroupManager = koinInject<IFamilyGroupManagerService>()
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()

        var firstname by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var familyGroupName by remember { mutableStateOf("") }
        var isCreatingFamilyGroup by remember { mutableStateOf(false) }

        StartScreen {
            CreateFamilyGroupHeader()
            Icon(
                Icons.Filled.AccountCircle,
                stringResource(Res.string.app_icon_alt),
                modifier = Modifier.size(125.dp),
                tint = AdditionalTheme.colors.firstOptionPrimaryColor
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstname,
                label = { Text(stringResource(Res.string.text_field_name_label)) },
                onValueChange = { firstname = it },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = surname,
                label = { Text(stringResource(Res.string.text_field_surname_label)) },
                onValueChange = { surname = it },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = familyGroupName,
                label = { Text(stringResource(Res.string.text_field_group_name_label)) },
                enabled = !isCreatingFamilyGroup,
                onValueChange = { familyGroupName = it },
            )

            if (isCreatingFamilyGroup) {
                FamilyGroupCreatingDialog()
            }

            InitialScreenButton(
                text = stringResource(Res.string.create_new_family_group_title)
            ) {
                isCreatingFamilyGroup = true
                coroutineScope.launch {
                    familyGroupManager.createFamilyGroup(familyGroupName)
                    isCreatingFamilyGroup = false
                    navigator.replaceAll(DebugScreenContextId())
                }
            }
        }
    }

    @Composable
    private fun CreateFamilyGroupHeader() {
        return Box(
            modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)
        ) {
            Headline1(
                stringResource(Res.string.family_group_create_screen_title),
                textAlign = TextAlign.Center,
            )
        }
    }

}