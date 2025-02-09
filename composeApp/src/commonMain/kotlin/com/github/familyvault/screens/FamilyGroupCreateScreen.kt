package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.Constants
import com.github.familyvault.components.Button
import com.github.familyvault.components.TextField
import com.github.familyvault.components.typography.Headline1
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.app_icon_alt
import familyvault.composeapp.generated.resources.family_group_create_screen_title
import familyvault.composeapp.generated.resources.next_button_content
import familyvault.composeapp.generated.resources.text_field_group_name_label
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import org.jetbrains.compose.resources.stringResource

class FamilyGroupCreateScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Constants.screenPaddingSize)
                .padding(top = Constants.largeSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Constants.largeSpacing)
        ) {
            Headline1(
                stringResource(Res.string.family_group_create_screen_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Icon(
                Icons.Filled.AccountCircle,
                stringResource(Res.string.app_icon_alt),
                modifier = Modifier.size(125.dp),
                tint = Constants.firstOptionPrimaryColor
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
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Button(
                    content = stringResource(Res.string.next_button_content),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navigator.push(InitialScreen())
                    }
                )
            }
        }
    }
}