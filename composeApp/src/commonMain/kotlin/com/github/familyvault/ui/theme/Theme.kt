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
    val onPrimaryContainerSecondColor: Color
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
        onPrimaryContainerSecondColor = Color(0xff6D6D6D)
    )
}

private val LocalCustomColorsDark = staticCompositionLocalOf {
    AdditionalColors(
        firstOptionPrimaryColor = Color(0xff006EFF),
        firstOptionSecondaryColor = Color(0xff002752),
        secondOptionPrimaryColor = Color(0xff47D2B6),
        secondOptionSecondaryColor = Color(0xff196255),
        borderColor = Color(0xFF282828),
        mutedColor = Color(0xFF4D4D4D),
        onMutedColor = Color(0xffB3B3B3),
        onPrimaryContainerSecondColor = Color(0xffB3B3B3)
    )
}

@Immutable
data class Spacing (
    val large: Dp,
    val medium: Dp,
    val small: Dp,
    val normalPadding: Dp,
    val screenPadding: Dp
)

private val LocalSpacing = staticCompositionLocalOf {
    Spacing(
        large = 40.dp,
        medium = 10.dp,
        small = 5.dp,
        normalPadding = 13.dp,
        screenPadding = 25.dp
    )
}

private val LocalRoundness = staticCompositionLocalOf {
    Roundness(normalPercent = 7)
}

@Immutable
data class Roundness(
    val normalPercent: Int
)


@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
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
        @Composable
        get() = if (isSystemInDarkTheme()) LocalCustomColorsDark.current else LocalCustomColorsLight.current
    val spacings: Spacing
        @Composable
        get() = LocalSpacing.current
    val roundness: Roundness
        @Composable
        get() = LocalRoundness.current
}
