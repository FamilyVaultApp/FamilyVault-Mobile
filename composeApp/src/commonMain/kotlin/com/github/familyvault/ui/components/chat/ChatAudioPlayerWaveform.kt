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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.familyvault.AppConfig
import com.github.familyvault.ui.theme.AdditionalTheme
import kotlin.random.Random

@Composable
fun ChatAudioPlayerWaveform(
    isPlaying: Boolean,
    isAuthor: Boolean,
) {
    val barCount = AppConfig.CHAT_AUDIO_PLAYER_BAR_COUNT

    val staticHeights = remember {
        List(barCount) { Random.nextFloat().coerceIn(0.3f, 0.7f) }
    }

    val heights = if (isPlaying) {
        val infiniteTransition = rememberInfiniteTransition()
        List(barCount) { index ->
            infiniteTransition.animateFloat(
                initialValue = staticHeights[index],
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = LinearEasing, delayMillis = index * 100),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    } else {
        staticHeights.map { mutableStateOf(it) }
    }

    Row(
        Modifier.height(AdditionalTheme.spacings.screenPadding).padding(end = AdditionalTheme.spacings.normalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small)
    ) {
        heights.forEach { anim ->
            Box(
                modifier = Modifier
                    .width(AdditionalTheme.spacings.small)
                    .fillMaxHeight(anim.value)
                    .background(
                        if (isAuthor) MaterialTheme.colorScheme.onPrimary else AdditionalTheme.colors.otherChatBubbleContentColor,
                        shape = MaterialTheme.shapes.extraSmall
                    )
            )
        }
    }
}