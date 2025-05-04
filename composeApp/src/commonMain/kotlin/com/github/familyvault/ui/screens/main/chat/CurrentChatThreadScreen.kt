package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.services.listeners.IChatMessagesListenerService
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.ui.components.chat.ChatInputField
import com.github.familyvault.ui.components.chat.ChatThreadSettingsButton
import com.github.familyvault.ui.components.chat.messageEntry.ChatMessageEntry
import com.github.familyvault.ui.components.overrides.TopAppBar
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class CurrentChatThreadScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val chatService = koinInject<IChatService>()
        val chatMessageListenerService = koinInject<IChatMessagesListenerService>()
        val familyGroupService = koinInject<IFamilyGroupService>()
        val currentChatState = koinInject<ICurrentChatState>()
        val mediaPicker = koinInject<IImagePickerService>()
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val isAtTop by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 } }
        val chatThreadManagers = remember { mutableListOf<String>() }
        var myUserData: FamilyMember? by remember { mutableStateOf(null) }
        val chatThread = requireNotNull(currentChatState.chatThread)

        LaunchedEffect(chatThread) {
            mediaPicker.clearSelectedImages()

            chatService.populateDatabaseWithLastMessages(chatThread.id)
            currentChatState.populateStateFromService()
            chatThreadManagers.addAll(chatService.retrievePublicKeysOfChatThreadManagers(chatThread.id))
            chatMessageListenerService.startListeningForNewMessage(chatThread.id) { _ ->
                coroutineScope.launch {
                    scrollToLastMessage(listState, currentChatState)
                }
            }
            myUserData = familyGroupService.retrieveMyFamilyMemberData()
            scrollToLastMessage(listState, currentChatState)
        }
        LaunchedEffect(isAtTop) {
            if (isAtTop && currentChatState.messages.isNotEmpty()) {
                val currentLookingMessage =
                    currentChatState.messages[listState.firstVisibleItemIndex]
                currentChatState.getNextPageFromService()
                listState.scrollToItem(currentChatState.messages.indexOf(currentLookingMessage))
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                chatMessageListenerService.unregisterAllListeners()
                currentChatState.clear()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    chatThread.name, actions = {
                        if (myUserData != null) {
                            if (chatThread.type === ChatThreadType.GROUP && myUserData?.id in chatThreadManagers && myUserData?.permissionGroup != FamilyGroupMemberPermissionGroup.Guest) {
                                ChatThreadSettingsButton {
                                    navigator.push(
                                        ChatThreadEditScreen(
                                            chatThread.type,
                                        )
                                    )
                                }
                            }
                        }
                    })
            }) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = listState,
                ) {
                    itemsIndexed(
                        items = currentChatState.messages,
                        key = { _, message -> message.id }) { index, message ->
                        ChatMessageEntry(
                            message,
                            prevMessage = currentChatState.messages.getOrNull(index - 1),
                            nextMessage = currentChatState.messages.getOrNull(index + 1)
                        )
                    }
                }
                ChatInputField(
                    onTextMessageSend = {
                        if (it.isEmpty()) {
                            return@ChatInputField
                        }

                        chatService.sendTextMessage(chatThread.id, it, respondToMessageId = "")
                    },
                    onVoiceMessageSend = {
                        if (it.isEmpty()) {
                            return@ChatInputField
                        }
                        chatService.sendVoiceMessage(chatThread.id, it)
                    },
                    onImageMessageSend = {
                        if (it.isEmpty()) {
                            return@ChatInputField
                        }

                        it.forEach { mediaByteArray ->
                            chatService.sendImageMessage(chatThread.id, mediaByteArray)
                        }
                    }
                )
            }
        }
    }

    private suspend fun scrollToLastMessage(
        listState: LazyListState, chatState: ICurrentChatState
    ) {
        if (chatState.messages.isNotEmpty()) {
            listState.scrollToItem(chatState.messages.lastIndex)
        }
    }
}