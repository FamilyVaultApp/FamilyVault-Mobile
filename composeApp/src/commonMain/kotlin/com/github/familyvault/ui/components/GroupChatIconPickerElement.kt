package com.github.familyvault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import familyvault.composeapp.generated.resources.chat_icon_icon_alt

@Composable
fun GroupChatIconPickerElement(
    icon: ImageVector,
    size: Dp = AdditionalTheme.sizing.userAvatarSize,
    isPicked: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isPicked) MaterialTheme.colorScheme.primaryContainer else AdditionalTheme.colors.borderColor

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            rememberVectorPainter(icon),
            contentDescription = stringResource(Res.string.chat_icon_icon_alt),
            modifier = Modifier
                .size(size)
                .padding(AdditionalTheme.spacings.medium),
            tint = if (isPicked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        )
    }
}