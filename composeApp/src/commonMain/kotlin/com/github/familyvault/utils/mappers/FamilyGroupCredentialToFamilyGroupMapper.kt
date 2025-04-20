package com.github.familyvault.utils.mappers

import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.FamilyGroup

object FamilyGroupCredentialToFamilyGroupMapper {
    fun map(familyGroupCredential: FamilyGroupCredential): FamilyGroup = FamilyGroup(
        contextId = familyGroupCredential.contextId,
        name = familyGroupCredential.name,
        isDefault = familyGroupCredential.isDefault
    )
}