package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.MessageItem
import com.github.familyvault.models.chat.ThreadId
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup

class ChatService(private val privMxClient: IPrivMxClient, private val familyGroupService: IFamilyGroupService, private val familyGroupSessionService: IFamilyGroupSessionService): IChatService {
    override suspend fun createThread(
        threadTag: String,
        threadName: String?
    ): ThreadId {
        val familyGroupMembersList = familyGroupService.retrieveFamilyGroupMembersList()
        val contextId = familyGroupSessionService.getContextId()

        val splittedFamilyGroupMembersList = splitFamilyMembers(familyGroupMembersList)
        val users = splittedFamilyGroupMembersList.first
        val managers = splittedFamilyGroupMembersList.second
        val threadId = privMxClient.createThread(contextId, users, managers, threadTag.encodeToByteArray(), (threadName ?: "").encodeToByteArray())
        return threadId
    }

    override fun retrieveAllThreadIds(): List<ThreadId> {
        val contextId = familyGroupSessionService.getContextId()
        val threadIdList = privMxClient.retrieveAllThreadIds(contextId, 0L, 100L)

        return threadIdList
    }

    override fun sendMessage(
        threadId: ThreadId,
        messageContent: String,
        respondToMessageId: String
    ) {
        privMxClient.sendMessage(messageContent, threadId, respondToMessageId)
    }

    override fun retrieveMessagesFromThread(threadId: ThreadId): List<MessageItem> {
        val messagesList = privMxClient.retrieveMessagesFromThread(threadId, 0L, 100L)
        return messagesList
    }

    private fun splitFamilyMembers(familyGroupMembers: List<FamilyMember>): Pair<List<Pair<String, String>>, List<Pair<String, String>>> {
        val guardians: MutableList<Pair<String, String>> = mutableListOf()
        val members: MutableList<Pair<String, String>> = mutableListOf()

        for (member in familyGroupMembers) {
            if (member.permissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                guardians.add(member.fullname to member.publicKey)
            } else {
                members.add(member.fullname to member.publicKey)
            }
        }

        return members to guardians
    }

}