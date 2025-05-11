package com.github.familyvault.ui.components.filesCabinet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.familyvault.services.IDocumentPickerService
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.ui.components.dialogs.CircularProgressIndicatorDialog
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.error_occurred_label
import familyvault.composeapp.generated.resources.file_cabinet_sending_files
import familyvault.composeapp.generated.resources.file_cabinet_upload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun DocumentUploadActionButton() {
    val documentPicker = koinInject<IDocumentPickerService>()
    val fileCabinetService = koinInject<IFileCabinetService>()
    
    var startDocumentPicker by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    FloatingActionButton(onClick = {
        startDocumentPicker = true
    }) {
        Icon(
            Icons.Filled.UploadFile, 
            stringResource(Res.string.file_cabinet_upload),
        )
    }

    if (startDocumentPicker) {
        LaunchedEffect(Unit) {
            try {
                val documents = documentPicker.pickDocumentsAndReturnByteArrays()

                if (documents.isNotEmpty()) {
                    isUploading = true
                    
                    withContext(Dispatchers.IO) {
                        val documentUrls = documentPicker.getSelectedDocumentUrls()

                        documentUrls.forEachIndexed { index, uri ->
                            val documentName = documentPicker.getDocumentNameFromUri(uri) ?: "document_$index.pdf"
                            val documentMimeType = documentPicker.getDocumentMimeTypeFromUri(uri) ?: "application/pdf"

                            documents.getOrNull(index)?.let { docBytes ->
                                fileCabinetService.sendDocumentToFamilyGroupStore(
                                    docBytes,
                                    documentName,
                                    documentMimeType
                                )
                            }
                        }
                    }
                    
                    isUploading = false
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                startDocumentPicker = false
            }
        }
    }
    
    if (isUploading) {
        CircularProgressIndicatorDialog(stringResource(Res.string.file_cabinet_sending_files))
    }
    
    errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text(stringResource(Res.string.error_occurred_label)) },
            text = { Text(error) },
            confirmButton = {
                TextButton(
                    onClick = { errorMessage = null }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
