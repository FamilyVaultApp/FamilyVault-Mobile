package com.github.familyvault.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.join_family_group_content
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnimatedNfcBeam() {
    val animationDuration = 1500
    val infiniteTransition = rememberInfiniteTransition()

    val color by infiniteTransition.animateColor(
        initialValue = AdditionalTheme.colors.mutedColor,
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = infiniteRepeatable(
            animation = tween(
                animationDuration,
                easing = LinearOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse,
        )
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                animationDuration,
                easing = LinearOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Icon(
        Icons.Filled.Sensors,
        modifier = Modifier.size(128.dp).scale(scale),
        contentDescription = stringResource(Res.string.join_family_group_content),
        tint = color
    )
}