package com.github.familyvault.database.familyGroupCredential

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FamilyGroupCredential(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val solutionId: String,
    val contextId: String,
    val publicKey: String,
    val privateKey: String,
    val isDefault: Boolean = false
)