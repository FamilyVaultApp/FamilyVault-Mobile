package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_attach_photo
import familyvault.composeapp.generated.resources.chat_recording_icon
import familyvault.composeapp.generated.resources.chat_send_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatInputField(onTextMessageSend: (message: String) -> Unit) {
    var textMessage by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(AdditionalTheme.spacings.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Dodaj załącznik */ }) {
            Icon(
                Icons.Outlined.Image,
                contentDescription = stringResource(Res.string.chat_attach_photo),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = { /* Nagrywanie głosu */ }) {
            Icon(
                Icons.Outlined.Mic,
                contentDescription = stringResource(Res.string.chat_recording_icon),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


        TextField(
            label = stringResource(Res.string.chat_send_message),
            modifier = Modifier.weight(1f).background(Color.Transparent),
            value = textMessage,
            onValueChange = { textMessage = it },
        )


        IconButton(onClick = {
            onTextMessageSend(textMessage)
            textMessage = ""
        }) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(Res.string.chat_send_message),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}