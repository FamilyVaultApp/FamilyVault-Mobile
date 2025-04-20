package com.github.familyvault.ui.screens.start.createFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.FamilyGroupNameForm
import com.github.familyvault.forms.FamilyGroupNewMemberForm
import com.github.familyvault.ui.components.BottomNextButton
import com.github.familyvault.ui.components.HeaderWithIcon
import com.github.familyvault.ui.components.formsContent.FamilyGroupNameFormContent
import com.github.familyvault.ui.components.formsContent.NewFamilyMemberFormContent
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.family_group_create_screen_title
import org.jetbrains.compose.resources.stringResource

class FamilyGroupCreateMemberAndNameScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val newFamilyMemberForm by remember { mutableStateOf(FamilyGroupNewMemberForm()) }
        val familyGroupNameForm by remember { mutableStateOf(FamilyGroupNameForm()) }

        StartScreenScaffold {
            CreateFamilyGroupHeader()

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                FamilyGroupCreateForm(newFamilyMemberForm, familyGroupNameForm)
                BottomNextButton(
                    text = stringResource(Res.string.create_new_family_group_title),
                    enabled = newFamilyMemberForm.isFormValid() && familyGroupNameForm.isFormValid()
                ) {
                    navigator.replaceAll(
                        FamilyGroupCreateAssignPrivateKeyPasswordScreen(
                            newFamilyMemberForm.formData,
                            familyGroupNameForm.formData,
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun CreateFamilyGroupHeader() {
        HeaderWithIcon(
            stringResource(Res.string.family_group_create_screen_title),
            Icons.Filled.Groups
        )
    }

    @Composable
    private fun FamilyGroupCreateForm(
        newFamilyMemberForm: FamilyGroupNewMemberForm,
        familyGroupNameForm: FamilyGroupNameForm,
        isFormEnabled: Boolean = true,
    ) {
        Column {
            NewFamilyMemberFormContent(newFamilyMemberForm, isFormEnabled)
            FamilyGroupNameFormContent(familyGroupNameForm, isFormEnabled)
        }
    }
}