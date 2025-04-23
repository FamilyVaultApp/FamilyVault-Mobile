package com.github.familyvault.ui.screens.main.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.ui.components.HorizontalScrollableRow
import com.github.familyvault.ui.components.tasks.TaskGroupCompleted
import com.github.familyvault.ui.components.tasks.TaskGroupPending
import com.github.familyvault.ui.components.tasks.TaskNewCategoryButton
import com.github.familyvault.ui.components.tasks.TasksCategoryButton
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun TasksContent() {
    Column(
        modifier = Modifier.padding(vertical = AdditionalTheme.spacings.screenPadding)
    ) {
        HorizontalScrollableRow(
            modifier = Modifier.padding(horizontal = AdditionalTheme.spacings.screenPadding)
        ) {
            TasksCategoryButton("Test1") {}
            TasksCategoryButton("Test2", selected = true) {}
            TasksCategoryButton("Test3") {}
            TaskNewCategoryButton { }
        }
        Column(
            modifier = Modifier.padding(AdditionalTheme.spacings.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            TaskGroupPending(
                "Test2", listOf(
                    Task(
                        id = "1",
                        name = "Milk",
                        description = "Description",
                        completed = false
                    ),
                    Task(
                        id = "2",
                        name = "Egg",
                        description = "Description",
                        completed = false
                    )
                )
            )
            TaskGroupCompleted(
                listOf(
                    Task(
                        id = "3",
                        name = "Flour",
                        description = "Description",
                        completed = true
                    ),
                    Task(
                        id = "4",
                        name = "Oil",
                        description = "Description",
                        completed = true
                    ),
                    Task(
                        id = "5",
                        name = "Petrol",
                        description = "Description",
                        completed = true
                    )
                )
            )
        }
    }
}