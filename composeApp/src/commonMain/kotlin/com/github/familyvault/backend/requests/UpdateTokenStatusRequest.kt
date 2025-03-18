package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenStatusRequest(private val token: String, private val status: Int)