package com.github.familyvault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.github.familyvault.models.OptionType
import com.github.familyvault.models.backgroundColor
import com.github.familyvault.models.iconTint

@Composable
fun HeaderIcon(
    icon: ImageVector,
    contentDescription: String? = "",
    size: Dp = 140.dp,
    type: OptionType = OptionType.First
) {
    val backgroundColor = type.backgroundColor(isSelected = false)
    val iconTint = type.iconTint(isSelected = false)

    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(size)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = rememberVectorPainter(icon),
                contentDescription = contentDescription,
                modifier = Modifier.size(size / 2),
                tint = iconTint
            )
        }
    }
}