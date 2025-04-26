package com.github.familyvault.ui.components.tasks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.tasks_add_new_category
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskNewCategoryButton(onClick: () -> Unit) {
    TasksCategoryButton(
        stringResource(Res.string.tasks_add_new_category),
        Icons.Filled.Add,
        onClick = onClick
    )
}