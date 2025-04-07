package com.github.familyvault.utils

import com.github.familyvault.models.AddFamilyMemberDataPayload

interface IPayloadEncryptor {
    fun encrypt(payload: AddFamilyMemberDataPayload): String
}