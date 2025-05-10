package com.github.familyvault.services.listeners

interface IFileCabinetListenerService: IListenerService {
    fun startListeningForNewFiles(storeId: String, onNewFileCreated: (ByteArray) -> Unit)

}