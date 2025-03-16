package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.FamilyMemberNewMemberFormData
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.components.privateKey.AssignPrivateKeyFormContent
import com.github.familyvault.ui.components.privateKey.PrivateKeyAssignPasswordHeader
import com.github.familyvault.ui.components.screen.StartScreenScaffold

class FamilyGroupJoinAssignPrivateKeyPasswordScreen(private val newFamilyMemberDraft: FamilyMemberNewMemberFormData) :
    Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val form by remember { mutableStateOf(PrivateKeyAssignPasswordForm()) }

        StartScreenScaffold {
            PrivateKeyAssignPasswordHeader()
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                AssignPrivateKeyFormContent(
                    form,
                )
                InitialScreenButton(
                    enabled = form.isFormValid(),
                ) {
                    navigator.replaceAll(FamilyGroupJoinNfc("test"))
                }
            }
        }
    }
}