package com.github.familyvault.ui.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.services.IDocumentPickerService
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.dialogs.CircularProgressIndicatorDialog
import com.github.familyvault.ui.components.overrides.Dialog
import com.github.familyvault.ui.components.overrides.NavigationBar
import com.github.familyvault.ui.screens.main.chat.ChatThreadEditScreen
import com.github.familyvault.ui.screens.main.tasks.task.TaskNewScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.ui.theme.AppTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_create_new
import familyvault.composeapp.generated.resources.file_cabinet_sending_files
import familyvault.composeapp.generated.resources.file_cabinet_upload
import familyvault.composeapp.generated.resources.task_new
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(ChatTab) {
            // Workaround błędu w Jetpack Compose powodujący to, że ekran nie dostostoswuje się
            // dynamicznie do motywu systemu. Zostanie naprawiony w jetpack compose 1.8.0-alpha06.
            // TODO: usunąć po naprawieniu blędu w compose.
            AppTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            ChatTab, FilesCabinetTab, TaskTab
                        )
                    },
                    floatingActionButton = {
                        FloatingCurrentTabActionButton()
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }

    @Composable
    private fun FloatingCurrentTabActionButton() {
        val tabNavigator = LocalTabNavigator.current

        when (tabNavigator.current) {
            is ChatTab -> FloatingChatActionButton()
            is FilesCabinetTab -> FloatingFileCabinetActionButton()
            is TaskTab -> FloatingTaskActionButton()
        }
    }

    @Composable
    private fun FloatingChatActionButton() {
        val navigator = LocalNavigator.currentOrThrow
        FloatingActionButton(onClick = {
            navigator.parent?.push(
                ChatThreadEditScreen(
                    ChatThreadType.GROUP
                )
            )
        }) {
            Icon(
                Icons.Filled.GroupAdd,
                stringResource(Res.string.chat_create_new),
            )
        }
    }

    @Composable
    private fun FloatingFileCabinetActionButton() {
        val imagePicker = koinInject<IImagePickerService>()
        val documentPicker = koinInject<IDocumentPickerService>()
        val fileCabinetService = koinInject<IFileCabinetService>()
        val currentTabIndex = FilesCabinetTab.selectedTabIndex
        val TAG = "FileCabinetUpload"

        var startImagePicker by remember { mutableStateOf(false) }
        var startDocumentPicker by remember { mutableStateOf(false) }
        var isUploading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        when (currentTabIndex) {
            0 -> { // Photos tab - only images, goes to Images store
                FloatingActionButton(onClick = {
                    startImagePicker = true
                }) {
                    Icon(
                        Icons.Filled.UploadFile,
                        stringResource(Res.string.file_cabinet_upload),
                    )
                }

                if (startImagePicker) {
                    LaunchedEffect(Unit) {
                        val images = imagePicker.pickImagesAndReturnByteArrays()

                        if (images.isNotEmpty()) {
                            isUploading = true

                            withContext(Dispatchers.IO) {
                                images.forEach { mediaByteArray ->
                                    fileCabinetService.sendImageToFamilyGroupStore(mediaByteArray)
                                }
                            }

                            isUploading = false
                        }

                        startImagePicker = false
                    }
                }
            }
            1 -> { // Documents tab - supports both documents and images, goes to Documents store
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
                            Log.d(TAG, "Starting document picker")
                            val documents = documentPicker.pickDocumentsAndReturnByteArrays()
                            Log.d(TAG, "Document picker returned ${documents.size} documents")

                            if (documents.isNotEmpty()) {
                                isUploading = true
                                
                                withContext(Dispatchers.IO) {
                                    // Get the list of selected document URLs
                                    val documentUrls = documentPicker.getSelectedDocumentUrls()
                                    Log.d(TAG, "Selected document URLs: ${documentUrls.size}")
                                    
                                    // Process each document with its metadata
                                    documentUrls.forEachIndexed { index, uri ->
                                        val documentName = documentPicker.getDocumentNameFromUri(uri) ?: "document_$index.pdf"
                                        val documentMimeType = documentPicker.getDocumentMimeTypeFromUri(uri) ?: "application/pdf"
                                        Log.d(TAG, "Processing document: $documentName ($documentMimeType)")
                                        
                                        // Send the document to the documents storage
                                        documents.getOrNull(index)?.let { docBytes ->
                                            Log.d(TAG, "Sending document to store: $documentName (${docBytes.size} bytes)")
                                            fileCabinetService.sendDocumentToFamilyGroupStore(
                                                docBytes,
                                                documentName,
                                                documentMimeType
                                            )
                                            Log.d(TAG, "Successfully sent $documentName to store")
                                        }
                                    }
                                }
                                
                                isUploading = false
                            } else {
                                Log.d(TAG, "No documents selected")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error in document upload", e)
                            errorMessage = "Error: ${e.message}"
                        } finally {
                            startDocumentPicker = false
                        }
                    }
                }
            }
        }

        if (isUploading) {
            CircularProgressIndicatorDialog(stringResource(Res.string.file_cabinet_sending_files))
        }
        
        errorMessage?.let { error ->
            AlertDialog(
                onDismissRequest = { errorMessage = null },
                title = { Text("Error") },
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

    @Composable
    private fun FloatingTaskActionButton() {
        val taskListState = koinInject<ITaskListState>()
        val navigator = LocalNavigator.currentOrThrow

        taskListState.selectedTaskList?.let {
            FloatingActionButton(onClick = {
                navigator.parent?.push(TaskNewScreen(requireNotNull(taskListState.selectedTaskList).id))
            }) {
                Icon(
                    Icons.Filled.AddTask,
                    stringResource(Res.string.task_new),
                )
            }
        }
    }
}
