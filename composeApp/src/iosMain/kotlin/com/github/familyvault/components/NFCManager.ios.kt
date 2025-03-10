package com.github.familyvault.components

import kotlinx.coroutines.flow.SharedFlow

actual class NFCManager {
    actual val tags: SharedFlow<String>
        get() = TODO("Not yet implemented")

    @Composable
    actual fun registerApp() {
    }

}

@Composable
actual fun getNFCManager(): NFCManager {
    TODO("Not yet implemented")
}