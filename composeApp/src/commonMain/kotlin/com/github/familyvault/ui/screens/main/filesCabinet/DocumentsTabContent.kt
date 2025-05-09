package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.services.IFileOpenerService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.ui.components.FullScreenImage
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.filesCabinet.LoadingCard
import com.github.familyvault.ui.components.filesCabinet.PdfCard
import com.github.familyvault.ui.components.filesCabinet.PhotoCard
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.loading
import familyvault.composeapp.generated.resources.file_cabinet_retry
import familyvault.composeapp.generated.resources.file_cabinet_no_documents
import familyvault.composeapp.generated.resources.file_cabinet_download_pdf_title
import familyvault.composeapp.generated.resources.file_cabinet_download_pdf_message
import familyvault.composeapp.generated.resources.file_cabinet_download
import familyvault.composeapp.generated.resources.cancel_button_content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun DocumentsTabContent() {
    val fileCabinetService = koinInject<IFileCabinetService>()
    val imagePicker = koinInject<IImagePickerService>()
    val fileOpener = koinInject<IFileOpenerService>()
    val coroutineScope = rememberCoroutineScope()
    
    var documentByteArrays by remember { mutableStateOf<List<ByteArray>>(emptyList()) }
    var documentNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var documentMimeTypes by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isInitializing by remember { mutableStateOf(false) }
    var fullScreenImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDownloadConfirmation by remember { mutableStateOf(false) }
    var pdfToDownload by remember { mutableStateOf<Pair<ByteArray, String>?>(null) }
    
    suspend fun loadDocuments() {
        isLoading = true
        errorMessage = null
        
        try {
            val storeId = fileCabinetService.retrieveFileCabinetDocumentsStoreId()
            
            val documents = withContext(Dispatchers.IO) {
                fileCabinetService.getDocumentsWithMetadataFromStore(
                    storeId = storeId,
                    limit = 50,
                    skip = 0
                )
            }
            
            documentByteArrays = documents.map { it.content }
            
            documentNames = documents.mapIndexed { index, doc ->
                val fileName = doc.fileName
                val isPdf = fileOpener.isPdfFile(doc.content)
                
                when {
                    !fileName.isNullOrBlank() -> fileName
                    isPdf -> "Document_${index}.pdf" 
                    else -> "File_${index}.jpg"
                }
            }
            
            documentMimeTypes = documents.mapIndexed { index, doc ->
                val mimeType = doc.mimeType
                val isPdf = fileOpener.isPdfFile(doc.content)
                
                when {
                    !mimeType.isNullOrBlank() -> mimeType
                    isPdf -> "application/pdf"
                    else -> "image/jpeg"
                }
            }
            
        } catch (e: IllegalStateException) {
            isInitializing = true
            
            withContext(Dispatchers.IO) {
                try {
                    fileCabinetService.ensureDocumentsStoreExists()
                    val storeId = fileCabinetService.retrieveFileCabinetDocumentsStoreId()
                    
                    val documents = fileCabinetService.getDocumentsWithMetadataFromStore(
                        storeId = storeId,
                        limit = 50,
                        skip = 0
                    )
                    
                    documentByteArrays = documents.map { it.content }
                    
                    documentNames = documents.mapIndexed { index, doc ->
                        val fileName = doc.fileName
                        val isPdf = fileOpener.isPdfFile(doc.content)
                        
                        when {
                            !fileName.isNullOrBlank() -> fileName
                            isPdf -> "Document_${index}.pdf" 
                            else -> "File_${index}.jpg"
                        }
                    }
                    
                    documentMimeTypes = documents.map { doc ->
                        val mimeType = doc.mimeType
                        val isPdf = fileOpener.isPdfFile(doc.content)
                        
                        when {
                            !mimeType.isNullOrBlank() -> mimeType
                            isPdf -> "application/pdf"
                            else -> "image/jpeg"
                        }
                    }
                    
                } catch (e: Exception) {
                    errorMessage = "Could not initialize documents storage: ${e.message}"
                    documentByteArrays = emptyList()
                }
            }
            
            isInitializing = false
        } catch (e: Exception) {
            errorMessage = "Error loading documents: ${e.message}"
            documentByteArrays = emptyList()
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
                if (isInitializing) "Initializing documents storage..." 
                else stringResource(Res.string.loading), 
                modifier = Modifier.fillMaxSize()
            )
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, 
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(errorMessage!!)
                Button(onClick = { 
                    coroutineScope.launch {
                        loadDocuments()
                    }
                }) {
                    Text(stringResource(Res.string.file_cabinet_retry))
                }
            }
        }
    } else if (documentByteArrays.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(stringResource(Res.string.file_cabinet_no_documents))
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(AdditionalTheme.spacings.small)
        ) {
            items(documentByteArrays.size) { index ->
                val documentBytes = documentByteArrays[index]
                val documentName = documentNames.getOrNull(index) ?: "Document_$index"
                
                if (fileOpener.isPdfFile(documentBytes)) {
                    PdfCard(
                        documentName = documentName,
                        onClick = {
                            pdfToDownload = Pair(documentBytes, documentName)
                            showDownloadConfirmation = true
                        }
                    )
                } else {
                    val imageBitmapState = produceState<ImageBitmap?>(initialValue = null, documentBytes) {
                        withContext(Dispatchers.IO) {
                            try {
                                value = imagePicker.getBitmapFromBytes(documentBytes)
                            } catch (e: Exception) {
                                value = null
                            }
                        }
                    }

                    val bitmap = imageBitmapState.value
                    if (bitmap == null) {
                        LoadingCard()
                    } else {
                        PhotoCard(
                            imageBitmap = bitmap,
                            onClick = { fullScreenImage = bitmap }
                        )
                    }
                }
            }
        }
    }

    if (showDownloadConfirmation && pdfToDownload != null) {
        AlertDialog(
            onDismissRequest = { 
                showDownloadConfirmation = false 
                pdfToDownload = null
            },
            title = { Text(stringResource(Res.string.file_cabinet_download_pdf_title)) },
            text = { Text(stringResource(Res.string.file_cabinet_download_pdf_message)) },
            confirmButton = {
                TextButton(onClick = {
                    pdfToDownload?.let { (bytes, name) ->
                        fileOpener.downloadFile(bytes, name)
                    }
                    showDownloadConfirmation = false
                    pdfToDownload = null
                }) {
                    Text(stringResource(Res.string.file_cabinet_download))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDownloadConfirmation = false
                    pdfToDownload = null
                }) {
                    Text(stringResource(Res.string.cancel_button_content))
                }
            }
        )
    }

    if (fullScreenImage != null) {
        FullScreenImage(
            imageBitmap = fullScreenImage!!,
            onDismiss = { fullScreenImage = null }
        )
    }
}