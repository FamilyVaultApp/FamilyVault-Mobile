package com.github.familyvault.utils

import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object PayloadDecryptor : IPayloadDecryptor {
    @OptIn(ExperimentalEncodingApi::class, ExperimentalSerializationApi::class)
    override fun decrypt(input: String): AddFamilyMemberDataPayload {
        val binaryInput = Base64.decode(input)
        return ProtoBuf.decodeFromByteArray(AddFamilyMemberDataPayload.serializer(), binaryInput)
    }
}