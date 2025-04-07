package com.github.familyvault.services

import androidx.compose.runtime.Composable
import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.coroutines.flow.Flow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NFCService {
    val tags: Flow<AddFamilyMemberDataPayload>

    @Composable
    fun RegisterApp()
    @Composable
    fun UnregisterApp()
    @Composable
    fun SetReadMode()
    @Composable
    fun SetEmulateMode(data: AddFamilyMemberDataPayload)
}
@Composable
expect fun getNFCService(): NFCService
