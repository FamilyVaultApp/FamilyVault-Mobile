package com.github.familyvault.ui.components.overrides

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import androidx.compose.material3.TopAppBar as MdTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  TopAppBar(
    title: String,
    icon: ImageVector? = null,
    actions: (@Composable () -> Unit)? = null
) {
    val navigator = LocalNavigator.currentOrThrow
    return MdTopAppBar(
        modifier = Modifier.shadow(
            AdditionalTheme.sizing.shadowSize,
        ),
        navigationIcon = {
            if(navigator.canPop) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, "", Modifier.clickable(true) {
                    navigator.pop()
                })
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
            ) {
                if (icon != null) {
                    Icon(
                        icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Paragraph(title)
            }
        },

        actions = {
            actions?.invoke()
        }
    )
}