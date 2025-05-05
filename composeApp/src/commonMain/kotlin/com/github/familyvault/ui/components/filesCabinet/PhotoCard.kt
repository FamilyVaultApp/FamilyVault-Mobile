package com.github.familyvault.ui.components.filesCabinet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_photo_card
import org.jetbrains.compose.resources.stringResource

@Composable
fun PhotoCard(
    imageBitmap: ImageBitmap,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(AdditionalTheme.spacings.small)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AdditionalTheme.roundness.normalPercent),
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = stringResource(Res.string.file_cabinet_photo_card),
            modifier = Modifier
                .fillMaxWidth()
                .height(AdditionalTheme.sizing.fileCabinetNormal)
                .padding(AdditionalTheme.spacings.small)
                .clip(RoundedCornerShape(AdditionalTheme.roundness.normalPercent)),
            contentScale = ContentScale.Crop
        )
    }
}