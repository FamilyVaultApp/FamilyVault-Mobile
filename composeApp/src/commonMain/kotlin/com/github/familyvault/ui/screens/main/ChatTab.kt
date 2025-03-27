package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.Paragraph
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_tab
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

object ChatTab : Tab {
    @Composable
    override fun Content() {

        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val privMxClient = koinInject<IPrivMxClient>()
        val contextId = familyGroupSessionService.getContextId()
        var threadIds by remember { mutableStateOf<List<String>>(emptyList()) }
        var isLoadingThreads by remember { mutableStateOf(true) }
        LaunchedEffect(Unit) {
            try {
                threadIds = privMxClient.retrieveAllThreadIds(contextId)
                isLoadingThreads = false
            } catch (e: Exception) {
                println("Failed to retrieve all thread IDs. Exception message: ${e.message}")
            }
        }

        Column {
            TopAppBar(
                stringResource(Res.string.chat_tab)
            )
            if (!isLoadingThreads) {
                Headline3("Retrieved thread IDs in this family group:")
                for (id in threadIds) {
                    Paragraph(id)
                }
            } else {
                LoaderWithText("Oczekiwanie...")
            }

        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.chat_tab)
            val icon = rememberVectorPainter(Icons.Filled.Folder)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}