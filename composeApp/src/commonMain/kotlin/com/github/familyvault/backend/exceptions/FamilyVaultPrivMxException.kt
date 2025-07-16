package com.github.familyvault.backend.exceptions

class FamilyVaultPrivMxException(
    val errorCode: Int,
    val errorMessage: String
) : RuntimeException("$errorCode - $errorMessage")