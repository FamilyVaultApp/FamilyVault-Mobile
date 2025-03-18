package com.github.familyvault.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.Dialog
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.error_occurred_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Text(
            text = stringResource(Res.string.error_occurred_label),
            modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center
        )
        Button(
            text = "Ok", onClick = onDismiss
        )
    }
}
