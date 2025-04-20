package com.github.familyvault.ui.components.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_add_multimedia_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun MultimediaSendButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = stringResource(Res.string.chat_add_multimedia_description),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}