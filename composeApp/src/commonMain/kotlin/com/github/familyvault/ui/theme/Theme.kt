package com.github.familyvault.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    surfaceContainer = surfaceContainerLight
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    surfaceContainer = surfaceContainerDark
)

@Immutable
data class AdditionalColors(
    val firstOptionPrimaryColor: Color,
    val firstOptionSecondaryColor: Color,
    val secondOptionPrimaryColor: Color,
    val secondOptionSecondaryColor: Color,
    val borderColor: Color,
    val mutedColor: Color,
    val onMutedColor: Color,
    val onPrimaryContainerSecondColor: Color,
    val otherChatBubbleColor: Color,
    val otherChatBubbleContentColor: Color,
    val dangerButtonBackgroundColor: Color,
    val dangerButtonContentColor: Color,
)

private val LocalCustomColorsLight = staticCompositionLocalOf {
    AdditionalColors(
        firstOptionPrimaryColor = Color(0xff368DFF),
        firstOptionSecondaryColor = Color(0xffE5F1FF),
        secondOptionPrimaryColor = Color(0xff40D0B3),
        secondOptionSecondaryColor = Color(0xffE1F8F4),
        borderColor = Color(0xFFE7E7E7),
        mutedColor = Color(0xFFB3B3B3),
        onMutedColor = Color(0xff6D6D6D),
        onPrimaryContainerSecondColor = Color(0xff6D6D6D),
        otherChatBubbleColor = Color(0xfff3f3f3),
        otherChatBubbleContentColor = Color(0xFF1C1B1F),
        dangerButtonBackgroundColor = Color(0xFFFF4C4C),
        dangerButtonContentColor = Color(0xFFFFFFFF)
    )
}

private val LocalCustomColorsDark = staticCompositionLocalOf {
    AdditionalColors(
        firstOptionPrimaryColor = Color(0xff006EFF),
        firstOptionSecondaryColor = Color(0xff002752),
        secondOptionPrimaryColor = Color(0xff47D2B6),
        secondOptionSecondaryColor = Color(0xff196255),
        borderColor = Color(0xFF282828),
        mutedColor = Color(0xFF858383),
        onMutedColor = Color(0xffB3B3B3),
        onPrimaryContainerSecondColor = Color(0xffB3B3B3),
        otherChatBubbleColor = Color(0xfff3f3f3),
        otherChatBubbleContentColor = Color(0xFF1C1B1F),
        dangerButtonBackgroundColor = Color(0xFFB00020),
        dangerButtonContentColor = Color(0xFFFFFFFF)
    )
}

@Immutable
data class Sizing(
    val veryLarge: Dp,
    val large: Dp,
    val medium: Dp,
    val small: Dp,
    val headerIconNormal: Dp,
    val entryMinSize: Dp,
    val taskListButtonHeight: Dp,
    val userAvatarSize: Dp,
    val shadowSize: Dp
)

@Immutable
data class Spacing(
    val veryLarge: Dp,
    val large: Dp,
    val medium: Dp,
    val small: Dp,
    val normalPadding: Dp,
    val screenPadding: Dp,
)

private val LocalSizing = staticCompositionLocalOf {
    Sizing(
        headerIconNormal = 80.dp,
        veryLarge = 400.dp,
        large = 250.dp,
        medium = 80.dp,
        small = 20.dp,
        entryMinSize = 65.dp,
        taskListButtonHeight = 40.dp,
        userAvatarSize = 40.dp,
        shadowSize = 4.dp
    )
}

private val LocalSpacing = staticCompositionLocalOf {
    Spacing(
        veryLarge = 60.dp,
        large = 40.dp,
        medium = 10.dp,
        small = 5.dp,
        normalPadding = 13.dp,
        screenPadding = 25.dp
    )
}

private val LocalRoundness = staticCompositionLocalOf {
    Roundness(
        normalPercent = 7,
        medium = 20.dp
    )
}

@Immutable
data class Roundness(
    val normalPercent: Int,
    val medium: Dp
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) darkScheme else lightScheme

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = AppTypography,
    )
}

object AdditionalTheme {
    val colors: AdditionalColors
        @Composable get() = if (isSystemInDarkTheme()) LocalCustomColorsDark.current else LocalCustomColorsLight.current
    val spacings: Spacing
        @Composable get() = LocalSpacing.current
    val sizing: Sizing
        @Composable get() = LocalSizing.current
    val roundness: Roundness
        @Composable get() = LocalRoundness.current
}
