package com.github.familyvault.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_download
import familyvault.composeapp.generated.resources.file_cabinet_download_pdf_message
import familyvault.composeapp.generated.resources.file_cabinet_download_pdf_title
import familyvault.composeapp.generated.resources.cancel_button_content
import org.jetbrains.compose.resources.stringResource

@Composable
fun PdfDownloadConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    fileName: String? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.file_cabinet_download_pdf_title)) },
        text = { 
            Text(
                if (fileName != null) {
                    "${stringResource(Res.string.file_cabinet_download_pdf_message)}\n$fileName" 
                } else {
                    stringResource(Res.string.file_cabinet_download_pdf_message)
                }
            ) 
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(Res.string.file_cabinet_download))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel_button_content))
            }
        }
    )
}
