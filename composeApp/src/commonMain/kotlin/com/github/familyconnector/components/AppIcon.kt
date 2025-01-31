package com.github.familyconnector.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import familyconnector.composeapp.generated.resources.Res
import familyconnector.composeapp.generated.resources.app_icon
import familyconnector.composeapp.generated.resources.app_icon_alt
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppIcon(iconSize: Dp = 150.dp, modifier: Modifier = Modifier) {
    Image(
        contentDescription = stringResource(Res.string.app_icon_alt),
        modifier = modifier.then(Modifier.size(iconSize)),
        painter = painterResource(Res.drawable.app_icon)
    )
}