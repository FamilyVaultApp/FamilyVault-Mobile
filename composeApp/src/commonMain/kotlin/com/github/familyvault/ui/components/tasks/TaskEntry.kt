package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerticalSplit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.components.typography.ParagraphMuted

@Composable
fun TaskEntry(task: Task, onCompletedChange: (Boolean) -> Unit) {
    val textStyle = if (task.completed) {
        TextStyle(textDecoration = TextDecoration.LineThrough)
    } else {
        TextStyle.Default
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(task.completed, onCheckedChange = onCompletedChange, enabled = !task.completed)
            Column {
                Paragraph(task.name, textStyle = textStyle)
                if (task.description.isNotEmpty()) {
                    ParagraphMuted(task.description, textStyle = textStyle)
                }
            }
        }
        AssignMemberToTaskButton {

        }
    }
}