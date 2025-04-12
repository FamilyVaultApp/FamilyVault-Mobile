package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.requests.GetMemberFromFamilyGroupRequest
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
    private val familyVaultBackendClient: IFamilyVaultBackendClient,
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

    override suspend fun createIndividualChatFromDraft(chatDraft: ChatThread): ChatThread {
        val contextId = familyGroupSessionService.getContextId()
        val currentUser = familyGroupSessionService.getCurrentUser()
        val familyMember = familyVaultBackendClient.getMemberFromFamilyGroup(
            GetMemberFromFamilyGroupRequest(
                contextId = contextId,
                userId = chatDraft.participantsIds.first { it.compareTo(currentUser.id) != 0 },
                publicKey = null,
            )
        ).member
        val threadUsers = listOf(familyMember.toPrivMxUser(), currentUser.toPrivMxUser())

        val threadId = privMxClient.createThread(
            contextId,
            users = threadUsers,
            managers = threadUsers,
            tag = AppConfig.CHAT_THREAD_TAG,
            type = ChatThreadType.INDIVIDUAL.toString(),
            name = threadUsers.joinToString { it.userId },
        )

        return ChatThread(
            id = threadId,
            name = familyMember.fullname,
            participantsIds = threadUsers.map { it.userId },
            lastMessage = null,
            type = ChatThreadType.INDIVIDUAL
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

    override fun retrieveExistsIndividualChatThreads(): List<ChatThread> {
        val chatThreads = retrieveAllChatThreads().filter { it.type == ChatThreadType.INDIVIDUAL }
        val currentUser = familyGroupSessionService.getCurrentUser()

        return chatThreads.map {
            it.copy(name = it.participantsIds.first { participantId ->
                participantId.compareTo(
                    currentUser.id
                ) != 0
            })
        }
    }

    override suspend fun retrieveAllIndividualChatThreads(): List<ChatThread> {
        val familyMembers = familyGroupService.retrieveFamilyGroupMembersList()

        val userPubKey = familyGroupSessionService.getPublicKey()
        val userId = familyMembers.first { it.toPrivMxUser().publicKey.compareTo(userPubKey) == 0 }
            .toPrivMxUser().userId

        val existsIndividualChatThreads = retrieveExistsIndividualChatThreads()

        val familyMembersWithoutIndividualChat = familyMembers.filter { familyMember ->
            !existsIndividualChatThreads.any {
                it.participantsIds.contains(
                    familyMember.toPrivMxUser().userId
                )
            }
        }

        val nonExistsIndividualChatThreads = familyMembersWithoutIndividualChat.map {
            ChatThread(
                id = null,
                it.fullname,
                listOf(it.toPrivMxUser().userId, userId),
                lastMessage = null,
                type = ChatThreadType.INDIVIDUAL_DRAFT,
            )
        }

        return (existsIndividualChatThreads + nonExistsIndividualChatThreads)
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