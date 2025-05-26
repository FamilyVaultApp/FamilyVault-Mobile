package com.github.familyvault.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.familyvault.ui.theme.AdditionalTheme

enum class OptionType {
    First, Second
}

@Composable
fun OptionType.borderColor(isSelected: Boolean): Color {
    return if (isSelected) if (this == OptionType.First) AdditionalTheme.colors.firstOptionPrimaryColor
    else AdditionalTheme.colors.secondOptionPrimaryColor
    else if (this == OptionType.First) AdditionalTheme.colors.borderColor
    else AdditionalTheme.colors.borderColor
}

@Composable
fun OptionType.backgroundColor(isSelected: Boolean): Color {
    return if (isSelected) if (this == OptionType.First) AdditionalTheme.colors.firstOptionPrimaryColor
    else AdditionalTheme.colors.secondOptionPrimaryColor
    else if (this == OptionType.First) AdditionalTheme.colors.firstOptionSecondaryColor
    else AdditionalTheme.colors.secondOptionSecondaryColor
}

@Composable
fun OptionType.iconTint(isSelected: Boolean): Color {
    return if (isSelected) if (this == OptionType.First) AdditionalTheme.colors.firstOptionSecondaryColor
    else AdditionalTheme.colors.secondOptionSecondaryColor
    else if (this == OptionType.First) AdditionalTheme.colors.firstOptionPrimaryColor
    else AdditionalTheme.colors.secondOptionPrimaryColor
}


