package com.github.familyvault.backend.client

import com.github.familyvault.backend.models.ThreadMessageItem
import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair

interface IPrivMxClient {
    fun generatePairOfPrivateAndPublicKey(password: String): PublicEncryptedPrivateKeyPair
    fun encryptPrivateKeyPassword(password: String): String
    fun decryptPrivateKeyPassword(encryptedPassword: String): String
    fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String)
    fun disconnect()

    /* Thread */
    fun createThread(
        contextId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        tag: String,
        type: String,
        name: String,
        referenceStoreId: String?
    ): String

    fun createStore(
        contextId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        type: String
    ): String
    fun updateThread(
        threadId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        newName: String? = null
    )

    fun retrieveThread(threadId: String) : ThreadItem
    fun retrieveAllThreads(contextId: String, startIndex: Int, pageSize: Int): List<ThreadItem>
    fun sendMessage(threadId: String, content: String, type: String, referenceMessageId: String = "")
    fun sendMessage(threadId: String, content: ByteArray, type: String, referenceMessageId: String = "")
    fun getFileAsByteArrayFromStore(fileId: String) : ByteArray
    fun sendByteArrayToStore(storeId: String, content: ByteArray) : String
    fun retrieveMessagesFromThread(
        threadId: String, startIndex: Int, pageSize: Int
    ): List<ThreadMessageItem>

    fun retrieveLastMessageFromThread(threadId: String): ThreadMessageItem?

    /* Listeners */
    fun unregisterAllEvents(eventName: String)
    fun registerOnMessageCreated(
        eventName: String,
        threadId: String,
        callback: (ThreadMessageItem) -> Unit
    )

    fun registerOnThreadCreated(eventName: String, callback: (ThreadItem) -> Unit)

    fun registerOnThreadUpdated(eventName: String, callback: (ThreadItem) -> Unit)
}