package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun TaskGroup(
    title: String,
    primary: Boolean,
    tasks: @Composable () -> Unit,
    actionButton: @Composable () -> Unit
) {
    val backgroundColor = if (primary) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    Column(
        modifier = Modifier.clip(RoundedCornerShape(AdditionalTheme.roundness.medium))
            .background(backgroundColor).padding(AdditionalTheme.spacings.medium).fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(AdditionalTheme.spacings.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Headline3(
                title,
                maxLines = 1,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
            actionButton()
        }
        tasks()
    }
}