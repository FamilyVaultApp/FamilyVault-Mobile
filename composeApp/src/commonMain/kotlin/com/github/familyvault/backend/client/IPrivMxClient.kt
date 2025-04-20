package com.github.familyvault.backend.client

import com.github.familyvault.backend.models.MessageItem
import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.ThreadItem
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
        name: String
    ): String

    fun retrieveAllThreads(contextId: String, startIndex: Int, pageSize: Int): List<ThreadItem>
    fun sendMessage(content: String, threadId: String, referenceMessageId: String = "")
    fun retrieveMessagesFromThread(
        threadId: String, startIndex: Int, pageSize: Int
    ): List<MessageItem>

    fun retrieveLastMessageFromThread(threadId: String): MessageItem?

    /* Listeners */
    fun unregisterAllEvents(eventName: String)
    fun registerOnMessageCreated(
        eventName: String,
        threadId: String,
        callback: (MessageItem) -> Unit
    )

    fun registerOnThreadCreated(eventName: String, callback: (ThreadItem) -> Unit)
}