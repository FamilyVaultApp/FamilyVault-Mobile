package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.GroupChatEditForm
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.FormSubmitState
import com.github.familyvault.models.enums.chat.ThreadIconType
import com.github.familyvault.models.enums.chat.icon
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.FamilyMemberEntry
import com.github.familyvault.ui.components.GroupChatIconPickerElement
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.main.MainScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.TextShortener
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_create_group_button_content
import familyvault.composeapp.generated.resources.chat_create_group_members
import familyvault.composeapp.generated.resources.chat_create_group_user_is_member
import familyvault.composeapp.generated.resources.chat_create_group_user_is_not_member
import familyvault.composeapp.generated.resources.chat_create_new
import familyvault.composeapp.generated.resources.chat_set_group_name
import familyvault.composeapp.generated.resources.chat_icon_icon_picker
import familyvault.composeapp.generated.resources.chat_save_edited_group_button_content
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

enum class GroupChatAction {
    Create,
    Edit
}

@Composable
fun GroupChatEdit(chatThread: ChatThread? = null) {
    val navigator = LocalNavigator.currentOrThrow
    val familyGroupService = koinInject<IFamilyGroupService>()
    val chatService = koinInject<IChatService>()
    var isLoadingFamilyMembers by remember { mutableStateOf(true) }
    var myPublicKey by remember { mutableStateOf("") }
    var myUserData: FamilyMember? by remember { mutableStateOf(null) }
    val familyMembers = remember { mutableStateListOf<FamilyMember>() }
    var createGroupChatState by remember { mutableStateOf(FormSubmitState.IDLE) }
    val form by remember { mutableStateOf(GroupChatEditForm()) }
    val groupChatAction = if (chatThread == null) GroupChatAction.Create else GroupChatAction.Edit
    val coroutineScope = rememberCoroutineScope()
    var selectedChatIconType by remember { mutableStateOf(ThreadIconType.GROUP)}

    val chatIconTypes = ThreadIconType.entries

    LaunchedEffect(Unit) {
        isLoadingFamilyMembers = true
        familyMembers.addAll(familyGroupService.retrieveFamilyGroupMembersWithoutMeList())
        myUserData = familyGroupService.retrieveMyFamilyMemberData()

        myPublicKey = myUserData!!.publicKey
        form.currentUserPublicKey = myPublicKey
        form.addMemberToGroupChat(myUserData!!)

        if (groupChatAction == GroupChatAction.Edit) {
            form.setGroupName(chatThread!!.name)
            val filteredMembers = familyMembers.filter { it.fullname in chatThread.participantsIds }
            form.addAllMembersFromListToGroupChat(filteredMembers)
            selectedChatIconType = chatThread.iconType?: ThreadIconType.GROUP
        }

        isLoadingFamilyMembers = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = if (groupChatAction == GroupChatAction.Create) stringResource(Res.string.chat_create_new) else chatThread!!.name,
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = AdditionalTheme.spacings.screenPadding)
                .padding(top = AdditionalTheme.spacings.screenPadding)
                .fillMaxWidth()
        ) {
            TextField(
                enabled = !isLoadingFamilyMembers && createGroupChatState != FormSubmitState.PENDING,
                value = form.groupName,
                onValueChange = { form.setGroupName(it) },
                label = stringResource(Res.string.chat_set_group_name),
                supportingText = { ValidationErrorMessage(form.groupNameValidationError) }
            )
            Headline3(stringResource(Res.string.chat_icon_icon_picker))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                chatIconTypes.forEach {
                    GroupChatIconPickerElement(it.icon, isPicked = selectedChatIconType == it) {
                        selectedChatIconType = it
                    }
                }
            }
            Headline3(stringResource(Res.string.chat_create_group_members))
            if (isLoadingFamilyMembers) {
                CircularProgressIndicator()
            } else {
                FamilyMemberSwitchItem(
                    member = myUserData!!,
                    isSelected = true,
                    onToggle = {},
                    isCurrentUser = true,
                    isCurrentlyMember = true
                )
                familyMembers.forEach { member ->
                    val isCurrentlyMember = (chatThread != null && chatThread.participantsIds.contains(member.id))
                    FamilyMemberSwitchItem(
                        member = member,
                        isSelected = form.familyMembers.contains(member),
                        onToggle = {
                            if (form.familyMembers.contains(member)) {
                                form.removeMemberFromGroupChat(member)
                            } else {
                                form.addMemberToGroupChat(member)
                            }
                        },
                        isCurrentUser = member.publicKey == myPublicKey,
                        isCurrentlyMember = isCurrentlyMember
                    )
                }
            }
            ValidationErrorMessage(form.groupMembersValidationError)
        }
        Column(
            modifier = Modifier.fillMaxHeight().padding(paddingValues)
                .padding(AdditionalTheme.spacings.screenPadding),
            verticalArrangement = Arrangement.Bottom,
        ) {
            CreateGroupChatButton(form.isFormValid() && !isLoadingFamilyMembers, groupChatAction, {
                createGroupChatState = FormSubmitState.PENDING
                coroutineScope.launch {
                    try {
                        if (groupChatAction == GroupChatAction.Create) {
                            chatService.createGroupChat(form.groupName, form.familyMembers, selectedChatIconType)
                        } else {
                            chatService.updateChatThread(
                                chatThread!!,
                                form.familyMembers,
                                form.groupName,
                                selectedChatIconType
                            )
                            createGroupChatState = FormSubmitState.IDLE
                            navigator.replaceAll(MainScreen())
                        }
                        createGroupChatState = FormSubmitState.IDLE
                        navigator.pop()
                    } catch (e: Exception) {
                        createGroupChatState = FormSubmitState.ERROR
                    }
                }
            })
        }
    }
}

@Composable
private fun FamilyMemberSwitchItem(
    member: FamilyMember,
    isSelected: Boolean,
    onToggle: () -> Unit,
    isCurrentUser: Boolean,
    isCurrentlyMember: Boolean
) {
    FamilyMemberEntry(member,
        additionalDescription = { FamilyMemberEntryAdditionalDescription(if (isCurrentlyMember) "CURRENT_MEMBER" else "NOT_CURRENT_MEMBER") }) {
        Switch(
            checked = isSelected || isCurrentUser,
            onCheckedChange = { onToggle() },
            enabled = !isCurrentUser
        )
    }
}


@Composable
private fun FamilyMemberEntryAdditionalDescription(additionalDescription: String)
{
    Paragraph(
        TextShortener.shortenText(
            when (additionalDescription) {
                "NOT_CURRENT_MEMBER" -> stringResource(Res.string.chat_create_group_user_is_not_member)
                "CURRENT_MEMBER" -> stringResource(Res.string.chat_create_group_user_is_member)
                else -> ""
            }
        ), color = when (additionalDescription) {
            "CURRENT_MEMBER" -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onBackground
        }
    )
}


@Composable
private fun CreateGroupChatButton(enabled: Boolean, groupChatAction: GroupChatAction, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        text = if (groupChatAction == GroupChatAction.Create) stringResource(Res.string.chat_create_group_button_content) else stringResource(Res.string.chat_save_edited_group_button_content),
        enabled = enabled,
        onClick = onClick
    )
}
