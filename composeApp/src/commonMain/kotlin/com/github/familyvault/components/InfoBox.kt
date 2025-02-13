package com.github.familyvault.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import com.github.familyvault.Constants
import com.github.familyvault.components.typography.Headline3
import com.github.familyvault.components.typography.Paragraph
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
                .clip(RoundedCornerShape(Constants.normalRoundPercent))
                .then(clickableModifier)
                .background(color = Constants.tertiaryColor)
                .padding(Constants.normalPaddingSize)
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Constants.normalPaddingSize),
        ) {
            Column {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(Res.string.info_icon_alt),
                    tint = Constants.primaryColor
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Constants.smallSpacing)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Headline3(title, color = Constants.primaryColor)
                    link?.let {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = stringResource(Res.string.open_link_alt),
                            tint = Constants.primaryColor
                        )
                    }
                }
                Paragraph(content)
            }
        }
    }
}
