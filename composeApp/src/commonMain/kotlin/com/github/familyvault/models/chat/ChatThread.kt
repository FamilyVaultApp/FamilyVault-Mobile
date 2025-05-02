package com.github.familyvault.models.chat

import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.models.enums.chat.ThreadIconType

data class ChatThread(
    val id: String,
    val name: String,
    val participantsIds: List<String>,
    val lastMessage: ChatMessage?,
    val type: ChatThreadType,
    val iconType: ThreadIconType? = null
) {
    fun customNameIfIndividualOrDefault(userId: String): String =
        if (type == ChatThreadType.INDIVIDUAL)
            participantsIds.firstOrNull { it != userId } ?: name else name
}