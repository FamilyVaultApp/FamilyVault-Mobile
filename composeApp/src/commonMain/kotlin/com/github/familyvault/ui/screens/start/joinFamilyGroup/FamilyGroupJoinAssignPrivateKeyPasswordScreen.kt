package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.NewFamilyMemberFormData
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.ui.components.privateKey.AssignPrivateKeyFormContent
import com.github.familyvault.ui.components.privateKey.PrivateKeyAssignPasswordHeader
import com.github.familyvault.ui.components.screen.StartScreenScaffold

class FamilyGroupJoinAssignPrivateKeyPasswordScreen(private val newFamilyMemberDraft: NewFamilyMemberFormData) :
    Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val form by remember { mutableStateOf(PrivateKeyAssignPasswordForm()) }

        StartScreenScaffold {
            PrivateKeyAssignPasswordHeader()
            AssignPrivateKeyFormContent(
                form,
            ) {
                navigator.replaceAll(FamilyGroupJoinNFC("test"))
            }
        }
    }
}