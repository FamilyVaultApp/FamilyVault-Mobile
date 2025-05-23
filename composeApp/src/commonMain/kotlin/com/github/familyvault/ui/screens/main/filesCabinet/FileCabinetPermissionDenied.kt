package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.components.HeaderIcon
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_permission_denied
import familyvault.composeapp.generated.resources.file_cabinet_permission_denied_header
import org.jetbrains.compose.resources.stringResource

@Composable
fun FileCabinetPermissionDenied() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            HeaderIcon(
                Icons.Outlined.Warning,
                size = AdditionalTheme.sizing.headerIconNormal
            )
            Headline3(
                stringResource(Res.string.file_cabinet_permission_denied_header),
                fontWeight = FontWeight.SemiBold
            )
            ParagraphMuted(
                stringResource(Res.string.file_cabinet_permission_denied),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(250.dp)
            )
        }
    }
}