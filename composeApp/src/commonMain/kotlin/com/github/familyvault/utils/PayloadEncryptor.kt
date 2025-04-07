package com.github.familyvault.utils

import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class, ExperimentalSerializationApi::class)
object PayloadEncryptor : IPayloadEncryptor {
    override fun encrypt(payload: AddFamilyMemberDataPayload): String {
        val binaryContent = ProtoBuf.encodeToByteArray(payload)
        return Base64.encode(binaryContent)
    }
}