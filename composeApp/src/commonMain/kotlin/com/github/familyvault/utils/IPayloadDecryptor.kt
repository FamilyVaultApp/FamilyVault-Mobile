package com.github.familyvault.utils

import com.github.familyvault.models.AddFamilyMemberDataPayload

interface IPayloadDecryptor {
    fun decrypt(input: String): AddFamilyMemberDataPayload
}