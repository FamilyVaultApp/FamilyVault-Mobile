package com.github.familyvault.states

import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.NewFamilyMemberData

interface IJoinFamilyGroupPayloadState {
    fun update(
        joinStatusToken: String,
        memberData: NewFamilyMemberData,
        encryptedPassword: String
    )

    fun getPayload(): AddFamilyMemberDataPayload
    fun getEncryptedPrivateKey(): String
}