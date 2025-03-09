package com.github.familyvault.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.CustomIcon
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.overrides.TextField
import com.github.familyvault.components.screen.StartScreenScaffold
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.forms.NewFamilyGroupMemberForm
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.join_family_group_title
import familyvault.composeapp.generated.resources.next_button_content
import familyvault.composeapp.generated.resources.text_field_name_label
import familyvault.composeapp.generated.resources.text_field_surname_label
import org.jetbrains.compose.resources.stringResource

class JoinFamilyGroupNameFormScreen: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val form = NewFamilyGroupMemberForm()

        StartScreenScaffold {
            JoinFamilyGroupHeader()
            CustomIcon(
                icon = Icons.Filled.Person
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                FamilyGroupCreateForm(form)
                InitialScreenButton(
                    text = stringResource(Res.string.next_button_content),
                    enabled = form.isFormValid()
                ) {
                    navigator.replaceAll(JoinFamilyGroupPasswordAssignScreen(form.formData))
                }
            }
        }
    }

    @Composable
    private fun JoinFamilyGroupHeader() {
        return Box(
            modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)
        ) {
            Headline1(
                stringResource(Res.string.join_family_group_title),
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    private fun FamilyGroupCreateForm(
        form: NewFamilyGroupMemberForm,
        isFormEnabled: Boolean = true,
    ) {
        Column {
            TextField(
                value = form.firstname,
                label = stringResource(Res.string.text_field_name_label),
                onValueChange = { form.setFirstname(it) },
                enabled = isFormEnabled,
            )
            TextField(
                value = form.surname,
                label = stringResource(Res.string.text_field_surname_label),
                onValueChange = { form.setSurname(it) },
                enabled = isFormEnabled,
            )
        }
    }
}