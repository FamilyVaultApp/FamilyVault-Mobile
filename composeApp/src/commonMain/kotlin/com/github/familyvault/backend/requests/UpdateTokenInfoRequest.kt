package com.github.familyvault.backend.requests

import com.github.familyvault.models.ContextIdInfo
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenInfoRequest(private val token: String, private val info: ContextIdInfo)