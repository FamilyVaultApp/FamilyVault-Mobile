package com.github.familyvault.backend.requests

import com.github.familyvault.models.ContextIdInfo
import com.github.familyvault.models.enums.JoinTokenStatus
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenStatusRequest(private val token: String, private val status: Int, private val contextIdInfo: ContextIdInfo?)