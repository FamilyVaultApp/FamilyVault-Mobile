package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatInputField() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AdditionalTheme.spacings.small)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Dodaj załącznik */ }) {
            Icon(Icons.Outlined.Image, contentDescription = "Attach image", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = { /* Nagrywanie głosu */ }) {
            Icon(Icons.Outlined.Mic, contentDescription = "Record audio", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            label = "Send message",
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { /* Wyślij wiadomość */ }) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Wyślij wiadomość", tint = MaterialTheme.colorScheme.primary)
        }
    }
}