package com.github.familyvault.ui.components.tasks

import androidx.compose.runtime.Composable
import com.github.familyvault.models.tasks.Task

@Composable
fun TaskGroupPending(categoryTitle: String, tasks: List<Task>) {
    TaskGroup(
        title = categoryTitle,
        primary = true,
        tasks = {
            tasks.map {
                TaskEntry(
                    it,
                    onCompletedChange = {}
                )
            }
        },
        actionButton = {
            TaskGroupConfigurationButton { }
        }
    )
}