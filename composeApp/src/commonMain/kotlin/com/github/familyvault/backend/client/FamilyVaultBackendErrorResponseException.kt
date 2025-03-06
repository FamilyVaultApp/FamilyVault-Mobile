package com.github.familyvault.backend.client

import io.ktor.http.HttpStatusCode

class FamilyVaultBackendErrorResponseException(
    statusCode: HttpStatusCode,
    errorBody: String
) : RuntimeException("$statusCode - $errorBody")
