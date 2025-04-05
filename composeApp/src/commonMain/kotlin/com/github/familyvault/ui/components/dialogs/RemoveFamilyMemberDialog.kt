package com.github.familyvault.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.cancel_button_content
import familyvault.composeapp.generated.resources.confirm_button_content
import familyvault.composeapp.generated.resources.remove_user_dialog
import org.jetbrains.compose.resources.stringResource

@Composable
fun RemoveFamilyMemberDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text(stringResource(Res.string.remove_user_dialog)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(Res.string.confirm_button_content))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel_button_content))
            }
        }
    )
}