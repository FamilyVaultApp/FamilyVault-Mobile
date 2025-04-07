package com.github.familyvault.backend.exceptions

class FamilyVaultPrivMxException(
    val errorCode: Long,
    val errorMessage: String
) : RuntimeException("$errorCode - $errorMessage")