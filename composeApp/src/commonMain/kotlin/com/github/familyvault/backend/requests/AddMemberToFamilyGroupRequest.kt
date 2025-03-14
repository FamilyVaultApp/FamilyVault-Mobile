package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddMemberToFamilyGroupRequest (val contextId: String, val userId: String, val userPubKey: String)