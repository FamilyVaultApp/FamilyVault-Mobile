package com.github.familyvault.ui.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.components.overrides.Dialog

@Composable
fun CircularProgressIndicatorDialog(
    message: String
) {
    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator(
            modifier = Modifier.width(48.dp)
        )
        Text(
            text = message,
            modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center,
        )
    }
}
