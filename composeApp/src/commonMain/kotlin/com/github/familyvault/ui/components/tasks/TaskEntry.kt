package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.components.typography.ParagraphMuted

@Composable
fun TaskEntry(
    task: Task,
    onCompletedClick: (Task) -> Unit,
    onEditClick: (Task) -> Unit,
    onAssignClick: (Task) -> Unit
) {
    val textStyle = if (task.content.completed) {
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = task.content.completed,
                onCheckedChange = { onCompletedClick(task) },
                colors = run {
                    val defaults = CheckboxDefaults.colors()
                    defaults.copy(
                        checkedBoxColor = defaults.disabledCheckedBoxColor,
                        checkedBorderColor = defaults.disabledBorderColor,
                        uncheckedBorderColor = defaults.checkedBorderColor,
                        checkedCheckmarkColor = defaults.checkedCheckmarkColor
                    )
                }
            )
            Column(
                modifier = Modifier.clickable(onClick = {
                    onEditClick(task)
                })
            ) {
                Paragraph(task.content.title, textStyle = textStyle)
                if (task.content.description.isNotEmpty()) {
                    ParagraphMuted(
                        task.content.description,
                        textStyle = textStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
            }
        }
        AssignMemberToTaskButton(task, onClick = { onAssignClick(task) })
    }
}