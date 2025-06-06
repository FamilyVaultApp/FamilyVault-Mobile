package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.FamilyGroupMemberForm
import com.github.familyvault.states.ICurrentDraftFamilyMemberState
import com.github.familyvault.ui.components.BottomNextButton
import com.github.familyvault.ui.components.HeaderWithIcon
import com.github.familyvault.ui.components.formsContent.FamilyMemberFormContent
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.join_family_group_title
import familyvault.composeapp.generated.resources.next_button_content
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject


class FamilyGroupJoinNewMemberScreen : Screen {

    @Composable
    override fun Content() {
        val draftFamilyMemberState = koinInject<ICurrentDraftFamilyMemberState>()
        val navigator = LocalNavigator.currentOrThrow
        val form by remember { mutableStateOf(FamilyGroupMemberForm()) }

        StartScreenScaffold {
            FamilyGroupJoinHeader()

            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom
            ) {
                FamilyMemberFormContent(form)
                BottomNextButton(
                    text = stringResource(Res.string.next_button_content),
                    enabled = form.isFormValid()
                ) {
                    draftFamilyMemberState.setFamilyMember(form.formData)
                    navigator.push(
                        FamilyGroupJoinAssignPrivateKeyPasswordScreen()
                    )
                }
            }
        }
    }

    @Composable
    private fun FamilyGroupJoinHeader() {
        HeaderWithIcon(
            stringResource(Res.string.join_family_group_title),
            Icons.Filled.Person
        )
    }
}