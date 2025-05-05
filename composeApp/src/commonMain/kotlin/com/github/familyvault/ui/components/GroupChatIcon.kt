package com.github.familyvault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun GroupChatIcon(icon: ImageVector, size: Dp = AdditionalTheme.sizing.userAvatarSize) {

    Box(
        modifier = Modifier.size(size)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .padding(AdditionalTheme.spacings.small / 2),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            rememberVectorPainter(icon), contentDescription = "",
            modifier = Modifier.size(size).padding(AdditionalTheme.spacings.small),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}