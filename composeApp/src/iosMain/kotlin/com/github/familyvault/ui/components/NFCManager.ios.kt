package com.github.familyvault.ui.components

import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.coroutines.flow.Flow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NFCManager {
    actual val tags: Flow<AddFamilyMemberDataPayload>
        get() = TODO("Not yet implemented")

    @Composable
    actual fun registerApp() {
    }

    @Composable
    actual fun unregisterApp() {
    }

    @Composable
    actual fun setReadMode() {
    }

    @Composable
    actual fun setEmulateMode(data: AddFamilyMemberDataPayload) {
    }

}

@Composable
actual fun getNFCManager(): NFCManager {
    TODO("Not yet implemented")
}