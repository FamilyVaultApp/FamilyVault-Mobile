package com.github.familyvault.models.enums.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

enum class ChatIconType(val value: Int) {
    GROUP(0),
    FAMILY(1),
    CHILDREN(2),
    GUARDIANS(3),
    WORK(4),
    SCHOOL(5),
}

val ChatIconType.icon: ImageVector
    get() = when (this) {
        ChatIconType.GROUP -> Icons.Default.Group
        ChatIconType.FAMILY -> Icons.Default.FamilyRestroom // lub inna pasująca
        ChatIconType.CHILDREN -> Icons.Default.ChildCare // z paczki Extended Icons
        ChatIconType.GUARDIANS -> Icons.Default.SupervisorAccount
        ChatIconType.WORK -> Icons.Default.Work
        ChatIconType.SCHOOL -> Icons.Default.School
    }