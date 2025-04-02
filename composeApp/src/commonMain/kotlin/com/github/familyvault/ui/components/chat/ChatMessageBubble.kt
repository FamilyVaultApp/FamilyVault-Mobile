package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatMessageBubble(
    senderName: String,
    message: String,
    isAuthor: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AdditionalTheme.spacings.small),
        horizontalArrangement = if (isAuthor) Arrangement.End else Arrangement.Start,
    ) {
        if (!isAuthor) {
            UserAvatar(firstName = senderName, size = AdditionalTheme.spacings.large)
        }
        Column(
            modifier = Modifier
                .padding(AdditionalTheme.spacings.small)
                .background(
                    if (isAuthor) MaterialTheme.colorScheme.primary else Color.LightGray,
                    shape = MaterialTheme.shapes.medium
                )
                .wrapContentWidth()
                .fillMaxWidth(0.5f)
                .padding(AdditionalTheme.spacings.medium),
            horizontalAlignment = if (isAuthor) Alignment.End else Alignment.Start
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = if (isAuthor) TextAlign.End else TextAlign.Start,
                color = if (isAuthor) MaterialTheme.colorScheme.onPrimary else Color.Black
            )
        }
    }
}