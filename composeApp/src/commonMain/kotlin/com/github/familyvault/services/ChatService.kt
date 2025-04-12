package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.ChatThreadType
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.utils.FamilyMembersSplitter
import com.github.familyvault.utils.mappers.MessageItemToChatMessageMapper
import com.github.familyvault.utils.mappers.MessageItemToStoredChatMessageMapper
import com.github.familyvault.utils.mappers.StoredChatMessageToChatMessageMapper

class ChatService(
    private val familyGroupService: IFamilyGroupService,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val privMxClient: IPrivMxClient,
    private val storedChatMessageRepository: IStoredChatMessageRepository,
) : IChatService {
    override suspend fun createGroupChat(
        name: String, members: List<FamilyMember>
    ): ChatThread {
        val contextId = familyGroupSessionService.getContextId()

        val splitFamilyGroupMembersList = FamilyMembersSplitter.split(members)
        val users = splitFamilyGroupMembersList.members.map { it.toPrivMxUser() }
        val managers = splitFamilyGroupMembersList.guardians.map { it.toPrivMxUser() }

        val threadId = privMxClient.createThread(
            contextId,
            users,
            managers,
            AppConfig.CHAT_THREAD_TAG,
            ChatThreadType.GROUP.toString(),
            name
        )
        return ChatThread(
            threadId,
            name,
            (users.map { it.userId } + managers.map { it.userId }).distinct(),
            lastMessage = null,
            ChatThreadType.GROUP,
        )
    }

    override fun retrieveAllChatThreads(): List<ChatThread> {
        val contextId = familyGroupSessionService.getContextId()
        val threadItems = privMxClient.retrieveAllThreads(contextId, 0, 100)

        return threadItems.map {
            ChatThread(
                it.threadId,
                it.privateMeta.name,
                (it.managers + it.users).distinct(),
                retrieveLastMessage(it.threadId),
                ChatThreadType.valueOf(it.publicMeta.type),
            )
        }
    }

    override fun retrieveAllGroupChatThreads(): List<ChatThread> {
        return retrieveAllChatThreads().filter { it.type == ChatThreadType.GROUP }
    }

    override fun retrieveAllIndividualChatThreads(): List<ChatThread> {
        val chatThreads = retrieveAllChatThreads().filter { it.type == ChatThreadType.INDIVIDUAL }
        val currentUser = familyGroupSessionService.getCurrentUser()

        return chatThreads.map {
            val chatName = it.participantsIds.firstOrNull { participantId ->
                participantId.compareTo(
                    currentUser.id
                ) != 0
            }

            it.copy(name = chatName ?: currentUser.fullname)
        }
    }

    override fun sendMessage(
        chatThreadId: String, messageContent: String, respondToMessageId: String
    ) {
        privMxClient.sendMessage(messageContent, chatThreadId, respondToMessageId)
    }

    override suspend fun createIndividualChat(
        firstMember: FamilyMember, secondMember: FamilyMember
    ) {
        val contextId = familyGroupSessionService.getContextId()

        val threadUsers =
            if (firstMember.id.compareTo(secondMember.id) == 0) listOf(firstMember.toPrivMxUser()) else listOf(
                firstMember.toPrivMxUser(), secondMember.toPrivMxUser()
            )

        privMxClient.createThread(
            contextId,
            users = threadUsers,
            managers = threadUsers,
            tag = AppConfig.CHAT_THREAD_TAG,
            type = ChatThreadType.INDIVIDUAL.toString(),
            name = threadUsers.joinToString { it.userId },
        )
    }

    override suspend fun createIndividualChatsWithAllFamilyMembersForMember(member: FamilyMember) {
        val familyMembers = familyGroupService.retrieveFamilyGroupMembersList()

        familyMembers.forEach {
            createIndividualChat(member, it)
        }
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