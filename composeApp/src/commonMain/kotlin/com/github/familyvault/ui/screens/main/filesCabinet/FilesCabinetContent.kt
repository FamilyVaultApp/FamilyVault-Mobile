package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_documents
import familyvault.composeapp.generated.resources.file_cabinet_photos
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilesCabinetContent(
    selectedTabIndex: Int = 0,
    onTabIndexChanged: (Int) -> Unit = {}
) {
    val tabTitles = listOf(
        stringResource(Res.string.file_cabinet_photos),
        stringResource(Res.string.file_cabinet_documents)
    )

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabIndexChanged(index) },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                PhotosTabContent()
            }

            1 -> {
                DocumentsTabContent()
            }
        }
    }
}