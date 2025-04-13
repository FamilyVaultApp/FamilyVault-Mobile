package com.github.familyvault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ParagraphStickyHeader(text: String) {
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AdditionalTheme.spacings.small)
        ) {
            Paragraph(text, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}