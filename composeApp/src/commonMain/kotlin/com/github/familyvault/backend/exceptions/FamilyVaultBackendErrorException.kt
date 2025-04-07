package com.github.familyvault.backend.exceptions

import io.ktor.http.HttpStatusCode

class FamilyVaultBackendErrorException(
    statusCode: HttpStatusCode,
    errorBody: String
) : RuntimeException("$statusCode - $errorBody")
