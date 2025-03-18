package com.github.familyvault.components

import androidx.compose.runtime.Composable
import com.github.familyvault.models.NewFamilyMemberData
import kotlinx.coroutines.flow.SharedFlow

sealed class NFCWriteStatus {
    object Success : NFCWriteStatus()
    data class Error(val message: String) : NFCWriteStatus()
}

expect class NFCManager {
    val tags: SharedFlow<NewFamilyMemberData>
    val writeStatus: SharedFlow<NFCWriteStatus>

    @Composable
    fun registerApp()

    fun prepareWrite(data: NewFamilyMemberData)
}

@Composable
expect fun getNFCManager(): NFCManager