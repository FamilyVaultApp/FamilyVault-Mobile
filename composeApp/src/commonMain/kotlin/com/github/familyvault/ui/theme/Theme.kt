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
    val mutedColor: Color,
    val borderColor: Color,
    val onMutedColor: Color,
    val onSecondaryContainerSecondColor: Color
)

private val LocalCustomColorsLight = staticCompositionLocalOf {
    AdditionalColors(
        firstOptionPrimaryColor = Color(0xff338cff),
        firstOptionSecondaryColor = Color(0xffe5f1ff),
        secondOptionPrimaryColor = Color(0xff5bd7be),
        secondOptionSecondaryColor = Color(0xffeafaf7),
        mutedColor = Color(0xFFB3B3B3),
        borderColor = Color(0xFFe6e6e6),
        onMutedColor = Color(0xff5f666d),
        onSecondaryContainerSecondColor = Color(0xff474C52)
    )
}

private val LocalCustomColorsDark = staticCompositionLocalOf {
    AdditionalColors(
        firstOptionPrimaryColor = Color(0xff0059cc),
        firstOptionSecondaryColor = Color(0xff000c1a),
        secondOptionPrimaryColor = Color(0xff28a48b),
        secondOptionSecondaryColor = Color(0xff051512),
        mutedColor = Color(0xFF4D4D4D),
        borderColor = Color(0xFF191919),
        onMutedColor = Color(0xff9299a0),
        onSecondaryContainerSecondColor = Color(0xffADB2B8)
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

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
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
