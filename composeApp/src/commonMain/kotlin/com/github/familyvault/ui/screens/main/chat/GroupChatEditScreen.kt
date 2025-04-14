package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.GroupChatEditForm
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.FormSubmitState
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.FamilyMemberEntry
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.screens.main.MainScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_create_group_button_content
import familyvault.composeapp.generated.resources.chat_create_group_members
import familyvault.composeapp.generated.resources.chat_create_new
import familyvault.composeapp.generated.resources.chat_set_group_name
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class GroupChatEditScreen(private val chatThread: ChatThread? = null) : Screen {

    enum class GroupChatAction {
        Create,
        Edit
    }

    @Composable
    override fun Content() {
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

        LaunchedEffect(Unit) {
            isLoadingFamilyMembers = true
            familyMembers.addAll(familyGroupService.retrieveFamilyGroupMembersWithoutMeList())
            myUserData = familyGroupService.retrieveMyFamilyMemberData()
            if (myUserData == null) {
                throw Exception("My user data is null")
            }

            myPublicKey = myUserData!!.publicKey
            form.currentUserPublicKey = myPublicKey
            form.addMemberToGroupChat(myUserData!!)

            if (groupChatAction == GroupChatAction.Edit) {
                form.setGroupName(chatThread!!.name)
                val filteredMembers = familyMembers.filter { it.fullname in chatThread.participantsIds }
                form.addAllMembersFromListToGroupChat(filteredMembers)
            }

            isLoadingFamilyMembers = false
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = if (groupChatAction == GroupChatAction.Create) stringResource(Res.string.chat_create_new) else chatThread!!.name,
                    showManagementButton = false
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
                Headline3(stringResource(Res.string.chat_create_group_members))
                if (isLoadingFamilyMembers) {
                    CircularProgressIndicator()
                } else {
                    FamilyMemberSwitchItem(
                        member = myUserData!!,
                        isSelected = true,
                        onToggle = {},
                        isCurrentMember = true
                    )
                    familyMembers.forEach { member ->
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
                            isCurrentMember = member.publicKey == myPublicKey
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
                CreateGroupChatButton(form.isFormValid() && !isLoadingFamilyMembers, {
                    createGroupChatState = FormSubmitState.PENDING
                    coroutineScope.launch {
                        try {
                            if (groupChatAction == GroupChatAction.Create) {
                                chatService.createGroupChat(form.groupName, form.familyMembers)
                            } else {
                                chatService.updateGroupChat(chatThread!!, form.familyMembers, form.groupName)
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
        isCurrentMember: Boolean
    ) {
        FamilyMemberEntry(member) {
            Switch(
                checked = isSelected || isCurrentMember,
                onCheckedChange = { onToggle() },
                enabled = !isCurrentMember
            )
        }
    }


    @Composable
    private fun CreateGroupChatButton(enabled: Boolean, onClick: () -> Unit) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.chat_create_group_button_content),
            enabled = enabled,
            onClick = onClick
        )
    }
}
