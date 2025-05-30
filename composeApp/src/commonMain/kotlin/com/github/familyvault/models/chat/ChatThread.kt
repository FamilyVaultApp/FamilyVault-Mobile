package com.github.familyvault.models.chat

import androidx.compose.runtime.Composable
import com.github.familyvault.models.MemberIdentifier
import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.models.enums.chat.ThreadIconType
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_private_notes
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource

data class ChatThread(
    val id: String,
    val name: String,
    val participantsIds: List<String>,
    val lastMessage: ChatMessage?,
    val type: ChatThreadType,
    val referenceStoreId: String?,
    val iconType: ThreadIconType? = null
) {
    fun isPrivateNote(): Boolean =
        type == ChatThreadType.INDIVIDUAL && participantsIds.size == 1

    @Composable
    fun customNameIfIndividualOrDefault(memberIdentifier: MemberIdentifier): String {
        if (isPrivateNote()) {
            return stringResource(Res.string.chat_private_notes)
        }

        return when (type) {
            ChatThreadType.INDIVIDUAL -> {
                val participantsIdentifier =
                    participantsIds.map { Json.decodeFromString<MemberIdentifier>(it) }
                return participantsIdentifier.firstOrNull { it != memberIdentifier }?.fullname
                    ?: name
            }

            ChatThreadType.GROUP -> name
        }
    }
}