package com.github.familyvault.components

import androidx.compose.runtime.Composable
import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.coroutines.flow.SharedFlow

sealed class NFCWriteStatus {
    object Success : NFCWriteStatus()
    data class Error(val message: String) : NFCWriteStatus()
}

expect class NFCManager {
    val tags: SharedFlow<AddFamilyMemberDataPayload>
    val writeStatus: SharedFlow<NFCWriteStatus>

    @Composable
    fun registerApp()
    @Composable
    fun unregisterApp()
    @Composable
    fun setReadMode()
    @Composable
    fun setIdleMode()
    @Composable
    fun setEmulateMode(data: AddFamilyMemberDataPayload)
}
@Composable
expect fun getNFCManager(): NFCManager
