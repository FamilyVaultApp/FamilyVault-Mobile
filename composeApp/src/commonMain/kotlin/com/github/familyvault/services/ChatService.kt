package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatImageMessageMetadata
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.models.enums.StoreType
import com.github.familyvault.models.enums.chat.ChatMessageContentType
import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.models.enums.chat.ThreadIconType
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.utils.FamilyMembersSplitter
import com.github.familyvault.utils.mappers.StoredChatMessageToChatMessageMapper
import com.github.familyvault.utils.mappers.ThreadMessageItemToChatMessageMapper
import com.github.familyvault.utils.mappers.ThreadMessageItemToStoredChatMessageMapper
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatService(
    private val familyGroupService: IFamilyGroupService,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val privMxClient: IPrivMxClient,
    private val storedChatMessageRepository: IStoredChatMessageRepository,
    private val imagePickerService: IImagePickerService,
) : IChatService {
    override suspend fun createGroupChat(
        name: String, members: List<FamilyMember>, chatIcon: ThreadIconType
    ): ChatThread {
        val contextId = familyGroupSessionService.getContextId()
        val currentUser = familyGroupSessionService.getCurrentUser()
        val splitFamilyGroupMembersList =
            FamilyMembersSplitter.splitWithProvidedMembersAsManagers(members, listOf(currentUser))
        val users = splitFamilyGroupMembersList.members.map { it.toPrivMxUser() }
        val managers = splitFamilyGroupMembersList.guardians.map { it.toPrivMxUser() }

        val storeId = privMxClient.createStore(
            contextId,
            users,
            managers,
            StoreType.CHAT_FILES.toString()
        )

        val threadId = privMxClient.createThread(
            contextId,
            users,
            managers,
            AppConfig.CHAT_THREAD_TAG,
            ChatThreadType.GROUP.toString(),
            name,
            storeId,
            chatIcon,
            listOf(currentUser.toPrivMxUser())
        )

        return ChatThread(
            threadId,
            name,
            (users.map { it.userId } + managers.map { it.userId }).distinct(),
            lastMessage = null,
            ChatThreadType.GROUP,
            referenceStoreId = storeId,
            chatIcon
        )
    }

    private suspend fun retrieveThreadInitialManagers(thread: ChatThread): List<FamilyMember> {
        val initialManagersPublicKeys = retrieveChatThreadInitialManagers(thread.id)
        return familyGroupService.retrieveFamilyGroupMembersList()
            .filter { initialManagersPublicKeys.contains(it.publicKey) }
    }

    override suspend fun updateChatThread(
        thread: ChatThread,
        members: List<FamilyMember>,
        newName: String?,
        chatIcon: ThreadIconType?,
        chatCreator: FamilyMember?
    ) {
        val initialManagers = retrieveThreadInitialManagers(thread).toMutableList()
        if (chatCreator != null) {
            initialManagers.add(chatCreator)
        }

        val splitFamilyGroupMembersList =
            FamilyMembersSplitter.splitWithProvidedMembersAsManagers(members, initialManagers)
        val users = splitFamilyGroupMembersList.members.map { it.toPrivMxUser() }
        val managers = splitFamilyGroupMembersList.guardians.map { it.toPrivMxUser() }
        privMxClient.updateThread(thread.id, users, managers, newName, chatIcon ?: thread.iconType)
        thread.referenceStoreId?.let {
            privMxClient.updateStore(it, users, managers)
        }

    }

    override fun retrieveAllChatThreads(): List<ChatThread> {
        val contextId = familyGroupSessionService.getContextId()
        val threadItems =
            privMxClient.retrieveAllThreadsWithTag(contextId, AppConfig.CHAT_THREAD_TAG, 0, 100)

        return threadItems.map {
            ChatThread(
                it.threadId,
                it.privateMeta.name,
                (it.managers + it.users).distinct(),
                retrieveLastMessage(it.threadId),
                ChatThreadType.valueOf(it.publicMeta.type),
                it.privateMeta.referenceStoreId,
                it.privateMeta.threadIcon
            )
        }
    }

    override fun retrieveAllGroupChatThreads(): List<ChatThread> =
        retrieveAllChatThreads().filter { it.type == ChatThreadType.GROUP }


    override fun retrieveAllIndividualChatThreads(): List<ChatThread> =
        retrieveAllChatThreads().filter { it.type == ChatThreadType.INDIVIDUAL }

    override fun sendTextMessage(
        chatThreadId: String, messageContent: String, respondToMessageId: String
    ) {
        privMxClient.sendMessage(
            chatThreadId,
            messageContent,
            ChatMessageContentType.TEXT.toString(),
            respondToMessageId
        )
    }

    override fun sendVoiceMessage(
        chatThreadId: String,
        audioData: ByteArray
    ) {
        val storeId =
            privMxClient.retrieveThread(chatThreadId).privateMeta.referenceStoreId ?: return

        val fileId = privMxClient.sendByteArrayToStore(storeId, audioData)

        privMxClient.sendMessage(chatThreadId, fileId, ChatMessageContentType.VOICE.toString())
    }

    override fun sendImageMessage(
        chatThreadId: String,
        imageByteArray: ByteArray
    ) {
        val storeId =
            requireNotNull(privMxClient.retrieveThread(chatThreadId).privateMeta.referenceStoreId)

        val rotatedAndCompressedImage =
            imagePickerService.compressAndRotateImage(imageByteArray)
        val imageSize = imagePickerService.getImageAsByteArraySize(rotatedAndCompressedImage)
        val fileId = privMxClient.sendByteArrayToStore(storeId, rotatedAndCompressedImage)

        val chatImageMessageMetadata =
            ChatImageMessageMetadata(fileId, imageSize.height, imageSize.width)

        privMxClient.sendMessage(
            chatThreadId,
            Json.encodeToString(chatImageMessageMetadata),
            ChatMessageContentType.IMAGE.toString()
        )
    }

    override fun getVoiceMessage(
        fileId: String
    ): ByteArray {
        return privMxClient.getFileAsByteArrayFromStore(fileId)
    }

    override fun getImageMessage(
        fileId: String
    ): ByteArray {
        return privMxClient.getFileAsByteArrayFromStore(fileId)
    }

    override fun getImageBitmap(chatMessage: String): ImageBitmap? {
        val bytes = getImageMessage(chatMessage)
        return imagePickerService.getBitmapFromBytes(bytes)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createIndividualChat(
        firstMember: FamilyMember, secondMember: FamilyMember
    ) {
        val contextId = familyGroupSessionService.getContextId()

        val threadUsers = if (firstMember.id == secondMember.id) {
            listOf(firstMember.toPrivMxUser())
        } else {
            listOf(
                firstMember.toPrivMxUser(), secondMember.toPrivMxUser()
            )
        }

        val storeId = privMxClient.createStore(
            contextId,
            users = threadUsers,
            managers = threadUsers,
            StoreType.CHAT_FILES.toString()
        )

        privMxClient.createThread(
            contextId,
            users = threadUsers,
            managers = threadUsers,
            tag = AppConfig.CHAT_THREAD_TAG,
            type = ChatThreadType.INDIVIDUAL.toString(),
            name = Uuid.random().toString(),
            referenceStoreId = storeId,
            threadInitialCreators = listOf(firstMember.toPrivMxUser(), secondMember.toPrivMxUser())
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

    override suspend fun retrievePublicKeysOfChatThreadManagers(threadId: String): List<String> {
        val threadItem = privMxClient.retrieveThread(threadId)

        return threadItem.managers
    }

    override suspend fun retrieveChatThreadInitialManagers(threadId: String): List<String> {
        val threadItem = privMxClient.retrieveThread(threadId)

        return threadItem.privateMeta.initialManagersPublicKeys ?: listOf()
    }

    override suspend fun updateGroupChatThreadsAfterUserPermissionChange(
        updatedUser: FamilyMember,
        familyMembers: List<FamilyMember>
    ) {
        val threadsList = retrieveAllChatThreads().filter { it.type == ChatThreadType.GROUP }
        for (thread in threadsList) {
            if (updatedUser.id in thread.participantsIds) {
                if (updatedUser.permissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                    updateChatThread(
                        thread,
                        familyMembers,
                        newName = null,
                        chatIcon = null,
                        updatedUser
                    )
                } else {
                    if (retrieveChatThreadInitialManagers(thread.id).contains(updatedUser.publicKey)) {
                        updateChatThread(
                            thread,
                            familyMembers,
                            newName = null,
                            chatIcon = null,
                            updatedUser
                        )
                    } else {
                        updateChatThread(thread, familyMembers, newName = null, chatIcon = null)
                    }
                }
            }

        }
    }

    override fun retrieveLastMessage(chatThreadId: String): ChatMessage? {
        val userPublicKey = familyGroupSessionService.getPublicKey()
        val message = privMxClient.retrieveLastMessageFromThread(chatThreadId) ?: return null

        return ThreadMessageItemToChatMessageMapper.map(message, userPublicKey)
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
                    ThreadMessageItemToStoredChatMessageMapper.map(privMxMessage, chatThreadId)
                )
            }
            currentPage++
        } while (!encounteredExistingMessage)
    }
}