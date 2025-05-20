package com.github.familyvault.models

import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.everyone
import org.jetbrains.compose.resources.stringResource

data class FamilyMemberPickerEntry(val familyMember: FamilyMember? = null) {
    @Composable
    fun getName(): String = familyMember?.fullname ?: stringResource(Res.string.everyone)
}
