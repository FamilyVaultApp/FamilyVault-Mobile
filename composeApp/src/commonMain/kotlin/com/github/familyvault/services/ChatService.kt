package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.utils.FamilyMembersSplitter
import com.github.familyvault.utils.mappers.MessageItemToChatMessageMapper
import com.github.familyvault.utils.mappers.MessageItemToStoredChatMessageMapper
import com.github.familyvault.utils.mappers.StoredChatMessageToChatMessageMapper

class ChatService(
    private val storedChatMessageRepository: IStoredChatMessageRepository,
    private val privMxClient: IPrivMxClient,
    private val familyGroupSessionService: IFamilyGroupSessionService
) : IChatService {
    override suspend fun createGroupChat(
        name: String, members: List<FamilyMember>
    ): ChatThread {
        val contextId = familyGroupSessionService.getContextId()

        val splitFamilyGroupMembersList = FamilyMembersSplitter.split(members)
        val users = splitFamilyGroupMembersList.members.map { it.toPrivMxUser() }
        val managers = splitFamilyGroupMembersList.guardians.map { it.toPrivMxUser() }

        val thread = privMxClient.createThread(
            contextId, users, managers, AppConfig.CHAT_THREAD_TAG, name
        )
        return ChatThread(name, thread.threadId, lastMessage = null)
    }

    override fun retrieveAllChatThreads(): List<ChatThread> {
        val contextId = familyGroupSessionService.getContextId()
        val threadIdList = privMxClient.retrieveAllThreads(contextId, 0, 100)

        return threadIdList.map {
            ChatThread(
                it.decodedThreadName, it.threadId, retrieveLastMessage(it.threadId)
            )
        }
    }

    override fun sendMessage(
        chatThreadId: String, messageContent: String, respondToMessageId: String
    ) {
        privMxClient.sendMessage(messageContent, chatThreadId, respondToMessageId)
    }

    override suspend fun retrieveMessagesFirstPage(chatThreadId: String): List<ChatMessage> {
        return retrieveMessagesPage(chatThreadId, 0)
    }

    override suspend fun retrieveMessagesPage(chatThreadId: String, page: Int): List<ChatMessage> {
        val userPublicKey = familyGroupSessionService.getPublicKey()
        val messagesList = storedChatMessageRepository.getStoredChatMessagesPage(chatThreadId, page)

        return messagesList.map {
            StoredChatMessageToChatMessageMapper.map(it, userPublicKey)
        }
    }

    override fun retrieveLastMessage(chatThreadId: String): ChatMessage? {
        val userPublicKey = familyGroupSessionService.getPublicKey()
        val message = privMxClient.retrieveLastMessageFromThread(chatThreadId) ?: return null

        return MessageItemToChatMessageMapper.map(message, userPublicKey)
    }

    override suspend fun populateDatabaseWithLastMessages(chatThreadId: String) {
        var encounteredExistingMessage = true
        var currentPage = 0

        do {
            val messagesFromPrivMx = privMxClient.retrieveMessagesFromThread(
                chatThreadId,
                currentPage * AppConfig.CHAT_MESSAGES_PER_PAGE,
                AppConfig.CHAT_MESSAGES_PER_PAGE
            )
            if (messagesFromPrivMx.isEmpty()) {
                break
            }

            for (privMxMessage in messagesFromPrivMx) {
                encounteredExistingMessage =
                    storedChatMessageRepository.isChatMessageInRepositoryById(privMxMessage.messageId)
                if (encounteredExistingMessage) {
                    return
                }
                storedChatMessageRepository.addNewStoredChatMessage(
                    MessageItemToStoredChatMessageMapper.map(privMxMessage, chatThreadId)
                )
            }
            currentPage++
        } while (!encounteredExistingMessage)
    }
}