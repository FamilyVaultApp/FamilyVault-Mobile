package com.github.familyvault.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.overrides.Dialog
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_creating
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatCreatingDialog() {
    Dialog(
        onDismissRequest = {}
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoaderWithText(stringResource(Res.string.chat_creating))
        }
    }
}