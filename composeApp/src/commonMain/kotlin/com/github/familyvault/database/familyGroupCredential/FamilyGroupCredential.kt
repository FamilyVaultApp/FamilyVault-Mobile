package com.github.familyvault.database.familyGroupCredential

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FamilyGroupCredential(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val familyGroupName: String,
    val solutionId: String,
    val contextId: String,
    val publicKey: String,
    val encryptedPrivateKey: String,
    val encryptedPrivateKeyPassword: String,
    val firstname: String,
    val lastname: String?,
    val backendUrl: String?,
    val isDefault: Boolean = false,
)