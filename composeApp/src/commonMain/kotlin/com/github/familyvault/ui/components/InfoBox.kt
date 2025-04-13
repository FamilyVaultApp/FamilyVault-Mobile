package com.github.familyvault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.info_icon_alt
import familyvault.composeapp.generated.resources.open_link_alt
import org.jetbrains.compose.resources.stringResource

@Composable
fun InfoBox(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    link: String? = null
) {
    val uriHandler = LocalUriHandler.current
    val clickableModifier = Modifier.clickable {
        link?.let {
            uriHandler.openUri(link)
        }
    }

    Box(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(AdditionalTheme.roundness.normalPercent))
                .then(clickableModifier)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(AdditionalTheme.spacings.normalPadding)
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.normalPadding),
        ) {
            Column {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(Res.string.info_icon_alt),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    link?.let {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = stringResource(Res.string.open_link_alt),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Paragraph(content, color = AdditionalTheme.colors.onPrimaryContainerSecondColor)
            }
        }
    }
}
