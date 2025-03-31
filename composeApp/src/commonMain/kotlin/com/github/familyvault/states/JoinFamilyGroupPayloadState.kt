package com.github.familyvault.states

import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.NewFamilyMemberData

class JoinFamilyGroupPayloadState : IJoinFamilyGroupPayloadState {
    private var payload: AddFamilyMemberDataPayload? = null
    private var encryptedPassword: String? = null

    override fun update(
        joinStatusToken: String, memberData: NewFamilyMemberData, encryptedPassword: String
    ) {
        payload = AddFamilyMemberDataPayload(memberData, joinStatusToken)
        this.encryptedPassword = encryptedPassword
    }


    override fun getPayload(): AddFamilyMemberDataPayload =
        requireNotNull(payload) { "Payload can't null" }

    override fun getEncryptedPrivateKey(): String =
        requireNotNull(encryptedPassword) { "Password can't be null" }
}