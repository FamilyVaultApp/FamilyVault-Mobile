package com.github.familyvault.models

data class FamilyGroup(
    val contextId: String,
    val name: String,
    val isDefault: Boolean,
    val firstname: String
)