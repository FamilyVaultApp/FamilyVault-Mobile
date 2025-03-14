package com.github.familyvault.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.familyvault.components.overrides.Button
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.creating_family_group_error
import familyvault.composeapp.generated.resources.creating_family_group_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyGroupCreatingDialog(
    state: DialogState,
    onDismiss: () -> Unit
) {
    if (state != DialogState.HIDDEN) {
        Dialog(
            onDismissRequest = {
                if (state == DialogState.ERROR) {
                    onDismiss()
                }
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().padding(16.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (state) {
                        DialogState.LOADING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.width(48.dp)
                            )
                            Text(
                                text = stringResource(Res.string.creating_family_group_label),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.Center),
                                textAlign = TextAlign.Center,
                            )
                        }

                        DialogState.ERROR -> {
                            Text(
                                text = stringResource(Res.string.creating_family_group_error),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                            Button(
                                text = "Ok",
                                onClick = onDismiss
                            )
                        }

                        DialogState.HIDDEN -> {
                            Unit
                        }
                    }
                }
            }
        }
    }
}

enum class DialogState {
    HIDDEN,
    LOADING,
    ERROR
}