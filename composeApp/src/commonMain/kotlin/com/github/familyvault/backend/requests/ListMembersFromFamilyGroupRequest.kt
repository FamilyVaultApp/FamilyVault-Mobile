package com.github.familyvault.backend.requests
import kotlinx.serialization.Serializable

@Serializable
data class ListMembersFromFamilyGroupRequest(val contextId: String)