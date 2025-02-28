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
)

@Immutable
data class AdditionalColors(
    val firstOptionPrimaryColor: Color,
    val firstOptionSecondaryColor: Color,
    val secondOptionPrimaryColor: Color,
    val secondOptionSecondaryColor: Color,
    val mutedColor: Color,
    val borderColor: Color
)

private val LocalCustomColorsLight = staticCompositionLocalOf {
    AdditionalColors(
        firstOptionPrimaryColor = Color(0xff378eff),
        firstOptionSecondaryColor = Color(0xffe5f1ff),
        secondOptionPrimaryColor = Color(0xff5ed8bf),
        secondOptionSecondaryColor = Color(0xffe1f8f4),
        mutedColor = Color(0xFF6d6d6d),
        borderColor = Color(0xFFe6e6e6)
    )
}

private val LocalCustomColorsDark = staticCompositionLocalOf {
    AdditionalColors(
        firstOptionPrimaryColor = Color(0xff2871d1),
        firstOptionSecondaryColor = Color(0xffc1cad6),
        secondOptionPrimaryColor = Color(0xff4dab98),
        secondOptionSecondaryColor = Color(0xffb8ccc8),
        mutedColor = Color(0xFFcccbda),
        borderColor = Color(0xFF262626)
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
