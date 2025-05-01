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
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun GroupChatIconPickerElement(icon: ImageVector, size: Dp = 40.dp, isPicked: Boolean = false) {

    Box(
        modifier = Modifier.size(size)
            .background(if (isPicked) MaterialTheme.colorScheme.primaryContainer else AdditionalTheme.colors.borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            rememberVectorPainter(icon), contentDescription = "",
            modifier = Modifier.size(size).padding(AdditionalTheme.spacings.small),
            tint = if (isPicked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground,
        )
    }
}