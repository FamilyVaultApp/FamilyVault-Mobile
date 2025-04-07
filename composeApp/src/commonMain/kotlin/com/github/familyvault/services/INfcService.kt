package com.github.familyvault.services

import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.coroutines.flow.Flow

interface INfcService {
    val tags: Flow<AddFamilyMemberDataPayload>

    suspend fun registerApp()

    fun unregisterApp()

    suspend fun setReadMode()

    suspend fun setEmulateMode(data: AddFamilyMemberDataPayload)
}
