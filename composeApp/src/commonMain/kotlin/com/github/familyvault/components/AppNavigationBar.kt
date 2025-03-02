package com.github.familyvault.components

import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyvault.screens.main.FilesCabinetScreen
import com.github.familyvault.screens.main.ChatsMainScreen
import com.github.familyvault.screens.main.TaskList
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.bottom_navigation_bar_cabinet
import familyvault.composeapp.generated.resources.bottom_navigation_bar_chats
import familyvault.composeapp.generated.resources.bottom_navigation_bar_task_board
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppNavigationBar(navigator: Navigator) {
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    val items = listOf(
        AppNavigationBarItem(stringResource(Res.string.bottom_navigation_bar_chats), Icons.AutoMirrored.Filled.Chat),
        AppNavigationBarItem(stringResource(Res.string.bottom_navigation_bar_task_board), Icons.Filled.Task),
        AppNavigationBarItem(stringResource(Res.string.bottom_navigation_bar_cabinet), Icons.Filled.Folder)
    )
    val screens = listOf(ChatsMainScreen(), TaskList(), FilesCabinetScreen())
    val navigationBarColors = NavigationBarItemDefaults.colors().copy(
        selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onBackground)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.border(1.dp, AdditionalTheme.colors.borderColor, RectangleShape)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = navigationBarColors,
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navigator.replace(screens[index])
                }
            )
        }
    }
}

private data class AppNavigationBarItem(val label: String, val icon: ImageVector)

