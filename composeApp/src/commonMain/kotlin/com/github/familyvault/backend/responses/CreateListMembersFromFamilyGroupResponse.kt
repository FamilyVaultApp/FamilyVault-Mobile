package com.github.familyvault.backend.responses

import com.github.familyvault.models.FamilyMember
import kotlinx.serialization.Serializable

@Serializable
data class CreateListMembersFromFamilyGroupResponse (val members: MutableList<FamilyMember>)