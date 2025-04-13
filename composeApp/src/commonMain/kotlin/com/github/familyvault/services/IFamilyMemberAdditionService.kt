package com.github.familyvault.services

import com.github.familyvault.models.NewFamilyMemberData

interface IFamilyMemberAdditionService {
    suspend fun addMemberToFamilyGroup(
        contextId: String, newFamilyMember: NewFamilyMemberData
    )

    suspend fun afterJoinedToFamilyMembersOperations()
}