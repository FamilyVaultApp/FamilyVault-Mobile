package com.github.familyvault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun UserAvatar(
    firstName: String,
    size: Dp? = null,
    modifier: Modifier = Modifier
) {
    return Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(size ?: AdditionalTheme.sizing.userAvatarSize)
            .background(MaterialTheme.colorScheme.primary)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        if (firstName.isNotEmpty()) {
            Text(
                firstName.first().toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}