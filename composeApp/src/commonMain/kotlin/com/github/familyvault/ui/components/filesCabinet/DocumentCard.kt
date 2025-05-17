package com.github.familyvault.ui.components.filesCabinet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.familyvault.models.fileCabinet.FileCabinetDocument
import com.github.familyvault.models.fileCabinet.isImage
import com.github.familyvault.models.fileCabinet.isPdf
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import kotlinx.datetime.format.Padding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

@Composable
fun DocumentCard(
    document: FileCabinetDocument,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            getDocumentPreview(document)?.let { imagePreview ->
                Image(
                    imagePreview,
                    contentDescription = document.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
        Column(
            modifier = Modifier.padding(AdditionalTheme.spacings.normalPadding)
        ) {
            Paragraph(document.name)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button("Otwórz") {
                    onClick()
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
private fun getDocumentPreview(document: FileCabinetDocument): ImageBitmap? {
    if (document.isPdf()) {
        return document.contentPreview?.decodeToImageBitmap()
    }

    if (document.isImage()) {
        return document.content.decodeToImageBitmap()
    }

    return null;
}
