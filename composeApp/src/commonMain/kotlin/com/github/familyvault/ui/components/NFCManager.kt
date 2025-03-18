package com.github.familyvault.components

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow

sealed class NFCWriteStatus {
    object Success : NFCWriteStatus()
    data class Error(val message: String) : NFCWriteStatus()
}

expect class NFCManager {
    val tags: SharedFlow<String>
    val writeStatus: SharedFlow<NFCWriteStatus>

    @Composable
    fun registerApp()

    fun prepareWrite(data: String)
}

@Composable
expect fun getNFCManager(): NFCManager