package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.familyvault.models.fileCabinet.FileCabinetDocument
import com.github.familyvault.models.fileCabinet.isImage
import com.github.familyvault.models.fileCabinet.isPdf
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.services.IFileOpenerService
import com.github.familyvault.ui.components.FullScreenImage
import com.github.familyvault.ui.components.HeaderIcon
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.dialogs.PdfDownloadConfirmationDialog
import com.github.familyvault.ui.components.filesCabinet.DocumentCard
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.error_occurred_label
import familyvault.composeapp.generated.resources.file_cabinet_error_initialize
import familyvault.composeapp.generated.resources.file_cabinet_error_loading
import familyvault.composeapp.generated.resources.file_cabinet_initializing_documents
import familyvault.composeapp.generated.resources.file_cabinet_no_documents
import familyvault.composeapp.generated.resources.file_cabinet_retry
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DocumentsTabContent() {
    val fileCabinetService = koinInject<IFileCabinetService>()
    val fileOpener = koinInject<IFileOpenerService>()
    val coroutineScope = rememberCoroutineScope()

    val documents = remember { mutableListOf<FileCabinetDocument>() }
    var isLoading by remember { mutableStateOf(true) }
    var isInitializing by remember { mutableStateOf(false) }
    var fullScreenImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var errorMessageKey by remember { mutableStateOf<String?>(null) }
    var showDownloadConfirmation by remember { mutableStateOf(false) }
    var pdfToDownload by remember { mutableStateOf<Pair<ByteArray, String>?>(null) }

    suspend fun loadDocuments() {
        isLoading = true
        errorMessageKey = null
        try {
            withContext(Dispatchers.IO) {
                documents.clear()
                documents.addAll(fileCabinetService.getDocumentsFromFileCabinetDocuments())
            }
            isInitializing = false
        } catch (e: Exception) {
            errorMessageKey = "file_cabinet_error_loading"
            documents.clear()
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadDocuments()
    }

    if (isLoading || isInitializing) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoaderWithText(
                if (isInitializing) stringResource(Res.string.file_cabinet_initializing_documents)
                else stringResource(Res.string.loading),
                modifier = Modifier.fillMaxSize()
            )
        }
        return
    }

    if (errorMessageKey != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    when (errorMessageKey) {
                        "file_cabinet_error_initialize" -> stringResource(Res.string.file_cabinet_error_initialize)
                        "file_cabinet_error_loading" -> stringResource(Res.string.file_cabinet_error_loading)
                        else -> stringResource(Res.string.error_occurred_label)
                    }
                )
                Button(onClick = {
                    coroutineScope.launch {
                        loadDocuments()
                    }
                }) {
                    Text(stringResource(Res.string.file_cabinet_retry))
                }
            }
        }
        return
    }

    if (documents.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
            ) {
                HeaderIcon(
                    Icons.Outlined.Folder,
                    size = AdditionalTheme.sizing.headerIconNormal
                )
                ParagraphMuted(
                    stringResource(Res.string.file_cabinet_no_documents),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(250.dp)
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(AdditionalTheme.spacings.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.screenPadding)
    ) {
        items(documents) {
            DocumentCard(it) {
                if (it.isPdf()) {
                    pdfToDownload = Pair(it.content, it.name)
                    showDownloadConfirmation = true
                }

                if (it.isImage()) {
                    fullScreenImage = it.content.decodeToImageBitmap()
                }
            }
        }
    }

    if (showDownloadConfirmation && pdfToDownload != null) {
        PdfDownloadConfirmationDialog(
            onDismiss = {
                showDownloadConfirmation = false
                pdfToDownload = null
            },
            onConfirm = {
                pdfToDownload?.let { (bytes, name) ->
                    fileOpener.downloadFile(bytes, name)
                }
                showDownloadConfirmation = false
                pdfToDownload = null
            },
            fileName = pdfToDownload?.second
        )
    }

    if (fullScreenImage != null) {
        FullScreenImage(
            imageBitmap = fullScreenImage!!,
            onDismiss = { fullScreenImage = null }
        )
    }
}