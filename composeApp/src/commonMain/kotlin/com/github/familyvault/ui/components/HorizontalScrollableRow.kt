package com.github.familyvault.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun HorizontalScrollableRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).then(modifier),
        horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
    ) {
        content()
    }
}