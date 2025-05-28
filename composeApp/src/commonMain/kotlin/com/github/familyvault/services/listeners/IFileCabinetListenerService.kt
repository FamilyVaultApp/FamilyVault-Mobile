package com.github.familyvault.services.listeners

import com.github.familyvault.models.fileCabinet.FileCabinetDocument

interface IFileCabinetListenerService: IListenerService {
    fun startListeningForNewFiles(storeId: String, onNewFileCreated: (ByteArray) -> Unit)
    fun startListeningForNewDocuments(storeId: String, onNewDocumentCreated: (FileCabinetDocument) -> Unit)
}