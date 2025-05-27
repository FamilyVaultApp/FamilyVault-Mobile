package com.github.familyvault.services.listeners

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.fileCabinet.FileCabinetDocument
import com.github.familyvault.utils.mappers.StoreByteArrayToDocumentsFileMapper
import kotlinx.serialization.json.Json

class FileCabinetListenerService(
    private val privMxClient: IPrivMxClient
) : IFileCabinetListenerService {
    companion object {
        const val CREATE_FILE_EVENT_NAME = "FILE_CABINET_FILE_CREATE"
        const val CREATE_DOCUMENT_EVENT_NAME = "DOCUMENT_FILE_CREATE"
    }

    override fun startListeningForNewFiles(storeId: String, onNewFileCreated: (ByteArray) -> Unit) {
        privMxClient.registerOnStoreFileCreated(CREATE_FILE_EVENT_NAME, storeId) {
            onNewFileCreated(
                it
            )
        }
    }

    override fun startListeningForNewDocuments(
        storeId: String,
        onNewDocumentCreated: (FileCabinetDocument) -> Unit
    ) {
        privMxClient.registerOnStoreFileCreated(CREATE_DOCUMENT_EVENT_NAME, storeId) {
            onNewDocumentCreated(
                StoreByteArrayToDocumentsFileMapper.map(
                    it
                )
            )
        }
    }


    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(CREATE_FILE_EVENT_NAME)
    }

}