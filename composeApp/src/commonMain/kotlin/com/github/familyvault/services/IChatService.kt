package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread

interface IChatService {
    fun retrieveAllChatThreads(): List<ChatThread>
    fun retrieveAllGroupChatThreads(): List<ChatThread>
    fun retrieveAllIndividualChatThreads(): List<ChatThread>
    fun retrieveLastMessage(chatThreadId: String): ChatMessage?
    fun sendTextMessage(chatThreadId: String, messageContent: String, respondToMessageId: String)
    fun sendVoiceMessage(chatThreadId: String, audioData: ByteArray)
    fun getVoiceMessage(fileId: String) : ByteArray
    suspend fun createGroupChat(name: String, members: List<FamilyMember>): ChatThread
    suspend fun createIndividualChat(firstMember: FamilyMember, secondMember: FamilyMember)
    suspend fun createIndividualChatsWithAllFamilyMembersForMember(member: FamilyMember)
    suspend fun populateDatabaseWithLastMessages(chatThreadId: String)
    suspend fun retrieveMessagesFirstPage(chatThreadId: String): List<ChatMessage>
    suspend fun retrieveMessagesPage(chatThreadId: String, page: Int): List<ChatMessage>
}