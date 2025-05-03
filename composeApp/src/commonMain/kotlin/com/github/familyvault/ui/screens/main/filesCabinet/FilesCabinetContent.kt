package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.tasks.TaskNewListButton
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_documents
import familyvault.composeapp.generated.resources.file_cabinet_photos
import familyvault.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilesCabinetContent() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf(
        stringResource(Res.string.file_cabinet_photos),
        stringResource(Res.string.file_cabinet_documents)
    )

    var isLoadingImages by remember { mutableStateOf(true) }
    var isLoadingDocuments by remember { mutableStateOf(true) }

    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> {
                if (isLoadingImages) {
                    isLoadingImages = false
                }
            }

            1 -> {
                if (isLoadingDocuments) {
                    isLoadingDocuments = false
                }
            }
        }
    }

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                if (isLoadingImages) {
                    LoaderWithText(
                        stringResource(Res.string.loading),
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    PhotosTabContent()
                }
            }

            1 -> {
                if (isLoadingDocuments) {
                    LoaderWithText(
                        stringResource(Res.string.loading),
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    DocumentsTabContent()
                }
            }
        }
    }
}