package com.github.familyvault.models.enums.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

enum class ThreadIconType(val value: Int) {
    GROUP(0),
    FAMILY(1),
    CHILDREN(2),
    GUARDIANS(3),
    WORK(4),
    SCHOOL(5),
}

val ThreadIconType.icon: ImageVector
    get() = when (this) {
        ThreadIconType.GROUP -> Icons.Default.Group
        ThreadIconType.FAMILY -> Icons.Default.FamilyRestroom
        ThreadIconType.CHILDREN -> Icons.Default.RocketLaunch
        ThreadIconType.GUARDIANS -> Icons.Default.BeachAccess
        ThreadIconType.WORK -> Icons.Default.Work
        ThreadIconType.SCHOOL -> Icons.Default.School
    }