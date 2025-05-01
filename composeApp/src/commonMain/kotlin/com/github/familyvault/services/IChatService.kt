package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.chat.ChatIconType

interface IChatService {
    fun retrieveAllChatThreads(): List<ChatThread>
    fun retrieveAllGroupChatThreads(): List<ChatThread>
    fun retrieveAllIndividualChatThreads(): List<ChatThread>
    fun retrieveLastMessage(chatThreadId: String): ChatMessage?
    fun sendTextMessage(chatThreadId: String, messageContent: String, respondToMessageId: String)
    fun sendVoiceMessage(chatThreadId: String, audioData: ByteArray)
    fun sendImageMessage(chatThreadId: String, imageByteArray: ByteArray)
    fun getVoiceMessage(fileId: String) : ByteArray
    fun getImageMessage(fileId: String) : ByteArray
    fun getImageBitmap(chatMessage: String): ImageBitmap?
    suspend fun createGroupChat(name: String, members: List<FamilyMember>, groupChatIcon: ChatIconType? = null): ChatThread
    suspend fun updateChatThread(thread: ChatThread, members: List<FamilyMember>, newName: String?, groupChatIcon: ChatIconType? = null)
    suspend fun createIndividualChat(firstMember: FamilyMember, secondMember: FamilyMember)
    suspend fun createIndividualChatsWithAllFamilyMembersForMember(member: FamilyMember)
    suspend fun populateDatabaseWithLastMessages(chatThreadId: String)
    suspend fun retrieveMessagesFirstPage(chatThreadId: String): List<ChatMessage>
    suspend fun retrieveMessagesPage(chatThreadId: String, page: Int): List<ChatMessage>
}