package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_create_group_button_content
import familyvault.composeapp.generated.resources.chat_create_new
import familyvault.composeapp.generated.resources.chat_create_group_default_name
import familyvault.composeapp.generated.resources.chat_set_group_name
import familyvault.composeapp.generated.resources.chat_create_group_members
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class CreateGroupChatScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        val chatService = koinInject<IChatService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val coroutineScope = rememberCoroutineScope()
        val defaultName = stringResource(Res.string.chat_create_group_default_name)
        var groupName by remember { mutableStateOf(defaultName) }
        var isCreatingGroupChat by remember { mutableStateOf(false) }
        var isLoadingFamilyMembers by remember { mutableStateOf(true) }
        val familyMembers = remember { mutableStateListOf<FamilyMember>() }
        val selectedMembers = remember { mutableStateListOf<FamilyMember>() }
        var myPublicKey by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            familyMembers.addAll(familyGroupService.retrieveFamilyGroupMembersList())
            myPublicKey = familyGroupSessionService.getPublicKey()
            familyMembers.find { it.publicKey == myPublicKey }?.let { selectedMembers.add(it) }
            isLoadingFamilyMembers = false
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = stringResource(Res.string.chat_create_new),
                    showManagementButton = false
                )
            },
        ) { paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.large),
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = AdditionalTheme.spacings.screenPadding)
                    .fillMaxWidth()
            ) {
                TextField(
                    enabled = !isCreatingGroupChat && !isLoadingFamilyMembers,
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = stringResource(Res.string.chat_set_group_name)
                )
                Headline3(stringResource(Res.string.chat_create_group_members))
                if (isLoadingFamilyMembers) {
                    CircularProgressIndicator()
                } else {
                    familyMembers.forEach { member ->
                        FamilyMemberSwitchItem(
                            member = member,
                            isSelected = selectedMembers.contains(member),
                            onToggle = {
                                if (selectedMembers.contains(member)) {
                                    selectedMembers.remove(member)
                                } else {
                                    selectedMembers.add(member)
                                }
                            }
                        )
                    }
                }

                CreateGroupChatButton(groupChatMembersValidator(selectedMembers, groupName, isCreatingGroupChat), {
                    isCreatingGroupChat = true
                    coroutineScope.launch {
                        chatService.createGroupChat(groupName, selectedMembers)
                        isCreatingGroupChat = false
                        navigator.pop()
                    }
                })
            }
        }
    }

    private fun groupChatMembersValidator(selectedMembers: List<FamilyMember>, groupName: String, isCreatingGroupChat: Boolean): Boolean {
        return groupName.isNotEmpty() && !isCreatingGroupChat && selectedMembers.isNotEmpty()
    }

    @Composable
    private fun FamilyMemberSwitchItem(
        member: FamilyMember,
        isSelected: Boolean,
        onToggle: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            UserAvatar(firstName = member.firstname)
            Paragraph(text = member.fullname, modifier = Modifier.weight(1f))
            Switch(
                checked = isSelected,
                onCheckedChange = { onToggle() }
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
