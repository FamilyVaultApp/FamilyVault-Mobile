package com.github.familyvault.backend

import io.ktor.http.HttpStatusCode

class FamilyVaultBackendException(
    statusCode: HttpStatusCode,
    errorBody: String
) : RuntimeException("$statusCode - $errorBody")
