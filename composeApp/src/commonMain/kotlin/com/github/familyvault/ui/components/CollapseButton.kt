package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.collapse
import familyvault.composeapp.generated.resources.expand
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollapseButton(isCollapsed: Boolean, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.size(AdditionalTheme.sizing.userAvatarSize),
        onClick = onClick
    ) {
        if (isCollapsed) {
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(Res.string.expand)
            )
        } else {
            Icon(
                Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(Res.string.collapse)
            )
        }
    }
}