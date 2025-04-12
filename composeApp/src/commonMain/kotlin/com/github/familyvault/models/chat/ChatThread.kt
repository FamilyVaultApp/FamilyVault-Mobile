package com.github.familyvault.models.chat

import com.github.familyvault.models.enums.ChatExistenceStatus
import com.github.familyvault.models.enums.ChatThreadType

data class ChatThread(
    val id: String?,
    val name: String,
    val participantsIds: List<String>,
    val lastMessage: ChatMessage?,
    val type: ChatThreadType,
)