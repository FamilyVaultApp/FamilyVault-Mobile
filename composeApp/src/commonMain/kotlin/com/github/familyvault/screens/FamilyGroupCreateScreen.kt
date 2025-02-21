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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.components.NextScreenButton
import com.github.familyvault.components.TextField
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.app_icon_alt
import familyvault.composeapp.generated.resources.family_group_create_screen_title
import familyvault.composeapp.generated.resources.text_field_group_name_label
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import org.jetbrains.compose.resources.stringResource

class FamilyGroupCreateScreen : Screen {
    @Composable
    override fun Content() {
        StartScreen {
            CreateFamilyGroupHeader()
            Icon(
                Icons.Filled.AccountCircle,
                stringResource(Res.string.app_icon_alt),
                modifier = Modifier.size(125.dp),
                tint = AdditionalTheme.colors.firstOptionPrimaryColor
            )
            //TODO : uzupelnic value i onValueChange
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(stringResource(Res.string.text_field_name_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(stringResource(Res.string.text_field_surname_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(stringResource(Res.string.text_field_group_name_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            NextScreenButton {}
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