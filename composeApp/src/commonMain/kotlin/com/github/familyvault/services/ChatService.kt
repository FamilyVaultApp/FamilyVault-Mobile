package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.utils.FamilyMembersSplitter
import com.github.familyvault.utils.mappers.MessageItemToChatMessageMapper

class ChatService(
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

    override fun retrieveMessages(chatThreadId: String): List<ChatMessage> {
        val userPublicKey = familyGroupSessionService.getPublicKey()
        val messagesList = privMxClient.retrieveMessagesFromThread(chatThreadId, 0, 100)

        return messagesList.map {
            MessageItemToChatMessageMapper.map(it, userPublicKey)
        }
    }

    override fun retrieveLastMessage(chatThreadId: String): ChatMessage? {
        val userPublicKey = familyGroupSessionService.getPublicKey()
        val message = privMxClient.retrieveLastMessageFromThread(chatThreadId) ?: return null

        return MessageItemToChatMessageMapper.map(message, userPublicKey)
    }
}