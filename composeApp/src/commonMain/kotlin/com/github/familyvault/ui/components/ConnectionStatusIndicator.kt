package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Pending
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
                Icon(Icons.Outlined.Pending, "Pending")
                Paragraph("Sprawdzam połączenie")
            }

            ConnectionStatus.OK -> {
                Icon(Icons.Outlined.Check, "Pending", tint = Color.Green)
                Paragraph("Poprawny serwer FamilyVault")
            }

            ConnectionStatus.FAILED -> {
                Icon(Icons.Outlined.Error, "Pending", tint = Color.Red)
                Paragraph("Nieudany test połączenia")
            }
        }
    }
}
