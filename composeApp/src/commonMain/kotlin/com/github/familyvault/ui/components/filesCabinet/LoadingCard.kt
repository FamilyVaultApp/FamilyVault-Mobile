package com.github.familyvault.ui.components.filesCabinet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun LoadingCard() {
    Card(
        modifier = Modifier
            .padding(AdditionalTheme.spacings.medium)
            .fillMaxWidth()
            .height(AdditionalTheme.sizing.fileCabinetNormal),
        shape = RoundedCornerShape(AdditionalTheme.roundness.normalPercent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}