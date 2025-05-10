package com.github.familyvault.services.listeners

import com.github.familyvault.backend.client.IPrivMxClient

class FileCabinetListenerService(
    private val privMxClient: IPrivMxClient
): IFileCabinetListenerService {
    companion object {
        const val CREATE_FILE_EVENT_NAME = "FILE_CABINET_FILE_CREATE"
    }

    override fun startListeningForNewFiles(storeId: String, onNewFileCreated: (ByteArray) -> Unit) {
        privMxClient.registerOnStoreFileCreated(CREATE_FILE_EVENT_NAME, storeId) {
            onNewFileCreated(
                it
            )
        }
    }

    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(CREATE_FILE_EVENT_NAME)
    }

}