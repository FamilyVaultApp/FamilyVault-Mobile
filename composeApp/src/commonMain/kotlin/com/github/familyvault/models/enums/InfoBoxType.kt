package com.github.familyvault.models.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class InfoBoxType {
    INFORMATION,
    DOCUMENTATION
}

@Composable
fun InfoBoxType.getIcon(): ImageVector {
    return when(this) {
        InfoBoxType.INFORMATION -> Icons.Outlined.Info
        InfoBoxType.DOCUMENTATION -> Icons.Outlined.Description
    }
}