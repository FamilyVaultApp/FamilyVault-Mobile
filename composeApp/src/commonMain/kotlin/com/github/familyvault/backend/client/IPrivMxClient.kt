package com.github.familyvault.backend.client

import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.backend.models.ThreadMessageItem
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
        referenceStoreId: String?,
        threadCreators: List<PrivMxUser>
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

    fun updateStore(
        storeId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
    )

    fun retrieveThread(threadId: String): ThreadItem
    fun retrieveAllThreads(contextId: String, startIndex: Int, pageSize: Int): List<ThreadItem>
    fun retrieveAllThreadsWithTag(
        contextId: String,
        tag: String,
        startIndex: Int,
        pageSize: Int
    ): List<ThreadItem>

    fun sendMessage(
        threadId: String,
        content: String,
        type: String,
        referenceMessageId: String = ""
    )

    fun sendMessage(
        threadId: String,
        content: ByteArray,
        type: String,
        referenceMessageId: String = ""
    )

    fun updateMessageContent(
        messageId: String,
        content: String,
    )

    fun getFileAsByteArrayFromStore(fileId: String): ByteArray
    fun getFilesAsByteArrayFromStore(storeId: String?, limit: Long, skip: Long): List<ByteArray>
    fun sendByteArrayToStore(storeId: String, content: ByteArray): String
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

    fun registerOnMessageUpdate(
        eventName: String,
        threadId: String,
        callback: (ThreadMessageItem) -> Unit
    )

    fun registerOnThreadCreated(eventName: String, callback: (ThreadItem) -> Unit)
    fun registerOnThreadUpdated(eventName: String, callback: (ThreadItem) -> Unit)
}