package com.github.familyvault.components

import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyvault.screens.FilesCabinetScreen
import com.github.familyvault.screens.HomeScreen
import com.github.familyvault.screens.TaskList
import com.github.familyvault.ui.theme.AdditionalTheme



@Composable
fun BottomNavigationBar(navigator: Navigator) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val selectedIcons = listOf(Icons.Filled.Chat, Icons.Filled.Task, Icons.Filled.Folder)
    val unselectedIcons =
        listOf(Icons.Outlined.Chat, Icons.Outlined.Task, Icons.Outlined.Folder)
    val items = listOf("Chat", "Tasks", "Cabinet")
    val screens = listOf(HomeScreen(), TaskList(), FilesCabinetScreen())
    val navigationBarDefaultColors = NavigationBarItemDefaults.colors().copy( selectedIconColor = AdditionalTheme.colors.firstOptionPrimaryColor, unselectedIconColor = MaterialTheme.colorScheme.onSurface )


    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = AdditionalTheme.colors.firstOptionPrimaryColor,
        modifier = Modifier.border(1.dp, AdditionalTheme.colors.borderColor, RectangleShape),
            ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = navigationBarDefaultColors,
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navigator.replace(screens[index])
                }
            )
        }
    }

}

