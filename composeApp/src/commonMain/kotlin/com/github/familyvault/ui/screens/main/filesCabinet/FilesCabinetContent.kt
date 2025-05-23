package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_documents
import familyvault.composeapp.generated.resources.file_cabinet_photos
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilesCabinetContent(
    selectedTabIndex: Int = 0,
    onTabIndexChanged: (Int) -> Unit = {},
    currentUserPermissionGroup: FamilyGroupMemberPermissionGroup
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
                    text = { 
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        ) 
                    }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> {
                if (currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guest) {
                    FileCabinetPermissionDenied()
                } else {
                    PhotosTabContent()
                }
            }

            1 -> {
                if (currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guest) {
                    FileCabinetPermissionDenied()
                } else {
                    DocumentsTabContent()
                }
            }
        }
    }
}