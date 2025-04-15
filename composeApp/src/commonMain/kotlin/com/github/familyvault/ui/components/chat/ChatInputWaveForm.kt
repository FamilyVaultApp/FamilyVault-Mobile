package com.github.familyvault.ui.components.chat

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatInputWaveForm(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val barCount = 35
    val barWidth = 2.dp
    val minHeight = 4f
    val maxHeight = 14f

    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val heights = List(barCount) { i ->
        infiniteTransition.animateFloat(
            initialValue = minHeight,
            targetValue = maxHeight,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400,
                    delayMillis = i * 40,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar$i"
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        heights.forEach { heightAnim ->
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(heightAnim.value.dp)
                    .background(
                        color = color,
                        shape = MaterialTheme.shapes.extraSmall
                    )
            )
        }
    }
}