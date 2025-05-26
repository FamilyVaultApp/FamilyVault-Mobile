package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.connection_checking
import familyvault.composeapp.generated.resources.connection_failed
import familyvault.composeapp.generated.resources.connection_ok
import org.jetbrains.compose.resources.stringResource

enum class ConnectionStatus {
    CONNECTING,
    OK,
    FAILED,
}

@Composable
fun ConnectionStatusIndicator(status: ConnectionStatus, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (status) {
            ConnectionStatus.CONNECTING -> {
                Icon(Icons.Outlined.Pending, stringResource(Res.string.connection_checking))
                Paragraph(stringResource(Res.string.connection_checking))
            }

            ConnectionStatus.OK -> {
                Icon(
                    Icons.Outlined.Check,
                    stringResource(Res.string.connection_ok),
                    tint = Color.Green
                )
                Paragraph(stringResource(Res.string.connection_ok))
            }

            ConnectionStatus.FAILED -> {
                Icon(
                    Icons.Outlined.Error,
                    stringResource(Res.string.connection_failed),
                    tint = Color.Red
                )
                Paragraph(stringResource(Res.string.connection_failed))
            }
        }
    }
}
