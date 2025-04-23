package com.github.familyvault.models.chat

import com.github.familyvault.models.enums.chat.ChatThreadType

data class ChatThread(
    val id: String,
    val name: String,
    val participantsIds: List<String>,
    val lastMessage: ChatMessage?,
    val type: ChatThreadType,
) {
    fun customNameIfIndividualOrDefault(userId: String): String =
        if (type == ChatThreadType.INDIVIDUAL)
            participantsIds.firstOrNull { it != userId } ?: name else name
}