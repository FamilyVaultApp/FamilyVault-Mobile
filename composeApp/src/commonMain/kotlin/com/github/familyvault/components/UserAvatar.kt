package com.github.familyvault.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun UserAvatar(
    firstName: String,
    size: Dp? = null
) {
    return Box(
        modifier = Modifier
            .size(size ?: 40.dp)
            .background(AdditionalTheme.colors.firstOptionSecondaryColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(firstName.first().toString())
    }
}