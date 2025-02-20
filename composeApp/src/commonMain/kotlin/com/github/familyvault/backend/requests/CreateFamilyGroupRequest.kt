package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateFamilyGroupRequest (val name: String, val description: String);