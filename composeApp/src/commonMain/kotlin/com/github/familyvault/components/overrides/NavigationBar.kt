package com.github.familyvault.components.overrides

import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.github.familyvault.ui.theme.AdditionalTheme
import androidx.compose.material3.NavigationBar as MdNavigationBar

@Composable
fun NavigationBar(vararg tabs: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val navigationBarColors = NavigationBarItemDefaults.colors().copy(
        selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onBackground
    )

    MdNavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.border(1.dp, AdditionalTheme.colors.borderColor, RectangleShape)
    ) {
        tabs.forEach { tab ->
            val options = tab.options
            NavigationBarItem(
                colors = navigationBarColors,
                icon = {
                    Icon(
                        options.icon ?: rememberVectorPainter(Icons.AutoMirrored.Filled.Article),
                        contentDescription = options.title
                    )
                },
                label = { Text(options.title) },
                selected = tabNavigator.current == tab,
                onClick = {
                    tabNavigator.current = tab
                }
            )
        }
    }
}

