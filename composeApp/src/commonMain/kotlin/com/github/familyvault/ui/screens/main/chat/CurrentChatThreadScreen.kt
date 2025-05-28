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
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.services.listeners.IChatMessagesListenerService
import com.github.familyvault.services.listeners.IChatThreadListenerService
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.states.ICurrentEditChatState
import com.github.familyvault.ui.components.chat.ChatInputField
import com.github.familyvault.ui.components.chat.ChatThreadSettingsButton
import com.github.familyvault.ui.components.chat.messageEntry.ChatMessageEntry
import com.github.familyvault.ui.components.dialogs.ErrorDialog
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.theme.AdditionalTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import familyvault.composeapp.generated.resources.chat_user_not_in_group
import familyvault.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

class CurrentChatThreadScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val chatService = koinInject<IChatService>()
        val chatMessageListenerService = koinInject<IChatMessagesListenerService>()
        val chatThreadListenerService = koinInject<IChatThreadListenerService>()
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val currentChatState = koinInject<ICurrentChatState>()
        val currentEditChatState = koinInject<ICurrentEditChatState>()
        val mediaPicker = koinInject<IImagePickerService>()
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val isAtTop by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 } }
        val isAtBottom by remember { derivedStateOf { 
            val layoutInfo = listState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItemsCount = layoutInfo.totalItemsCount
            
            lastVisibleItemIndex == 0 || 
            (totalItemsCount > 0 && listState.firstVisibleItemIndex <= (totalItemsCount * 0.05).toInt())
        } }
        val chatThreadManagers = remember { mutableListOf<String>() }
        var myUserData: FamilyMember? by remember { mutableStateOf(null) }
        var showErrorDialog by remember { mutableStateOf(false) }
        val chatThread = requireNotNull(currentChatState.chatThread)


        LaunchedEffect(chatThread) {
            mediaPicker.clearSelectedImages()
            try {
                chatService.populateDatabaseWithLastMessages(chatThread.id)
                currentChatState.populateStateFromService()
                chatThreadManagers.addAll(
                    chatService.retrievePublicKeysOfChatThreadManagers(
                        chatThread.id
                    )
                )
                chatThreadListenerService.startListeningForUpdatedChatThread {
                    coroutineScope.launch {
                        if (it.id != chatThread.id){
                            return@launch
                        }

                        currentChatState.update(it)
                    }
                }
                chatMessageListenerService.startListeningForNewMessage(chatThread.id) { _ ->
                    coroutineScope.launch {
                        val wasAtBottom = isAtBottom
                        currentChatState.populateStateFromService()
                        if (wasAtBottom) {
                            kotlinx.coroutines.delay(50)
                            scrollToLastMessage(listState, currentChatState)
                        }
                    }
                }
            } catch (e: Exception) {
                showErrorDialog = true
            }
            myUserData = familyGroupService.retrieveMyFamilyMemberData()
            scrollToLastMessage(listState, currentChatState)
        }
        LaunchedEffect(isAtTop) {
            if (isAtTop && currentChatState.messages.isNotEmpty()) {
                val currentLookingMessage =
                    currentChatState.messages[listState.firstVisibleItemIndex]
                currentChatState.getNextPageFromService()
                val newIndex = currentChatState.messages.indexOf(currentLookingMessage)
                if (newIndex != -1) {
                    listState.scrollToItem(newIndex)
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                chatMessageListenerService.unregisterAllListeners()
                chatThreadListenerService.unregisterAllListeners()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    chatThread.customNameIfIndividualOrDefault(familyGroupSessionService.getCurrentUser().identifier),
                    actions = {
                        if (myUserData != null) {
                            if (chatThread.type === ChatThreadType.GROUP && myUserData?.id in chatThreadManagers && myUserData?.permissionGroup != FamilyGroupMemberPermissionGroup.Guest) {
                                ChatThreadSettingsButton {
                                    currentEditChatState.updateChatToEdit(
                                        requireNotNull(
                                            currentChatState.chatThread
                                        )
                                    )
                                    navigator.push(
                                        ChatThreadEditScreen(
                                            chatThread.type
                                        )
                                    )
                                }
                            }
                        }
                    })
            }) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(AdditionalTheme.spacings.medium)
            ) {
                if (!showErrorDialog) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        state = listState,
                        reverseLayout = true
                    ) {
                        itemsIndexed(
                            items = currentChatState.messages.reversed(),
                            key = { _, message -> message.id }) { index, message ->
                            val originalIndex = currentChatState.messages.lastIndex - index
                            ChatMessageEntry(
                                message,
                                prevMessage = currentChatState.messages.getOrNull(originalIndex + 1),
                                nextMessage = currentChatState.messages.getOrNull(originalIndex - 1)
                            )
                        }
                    }
                    ChatInputField(
                        onTextMessageSend = {
                            if (it.isEmpty()) {
                                return@ChatInputField
                            }

                            chatService.sendTextMessage(chatThread.id, it, respondToMessageId = "")
                            coroutineScope.launch {
                                scrollToLastMessage(listState, currentChatState)
                            }
                        },
                        onVoiceMessageSend = {
                            if (it.isEmpty()) {
                                return@ChatInputField
                            }
                            chatService.sendVoiceMessage(chatThread.id, it)
                            coroutineScope.launch {
                                scrollToLastMessage(listState, currentChatState)
                            }
                        },
                        onImageMessageSend = {
                            if (it.isEmpty()) {
                                return@ChatInputField
                            }

                            it.forEach { mediaByteArray ->
                                chatService.sendImageMessage(chatThread.id, mediaByteArray)
                            }
                            coroutineScope.launch {
                                scrollToLastMessage(listState, currentChatState)
                            }
                        }
                    )

                } else {
                    ErrorDialog(
                        stringResource(Res.string.chat_user_not_in_group)
                    ) { navigator.pop() }
                }

            }
        }
    }

    private suspend fun scrollToLastMessage(
        listState: LazyListState, chatState: ICurrentChatState
    ) {
        if (chatState.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
}