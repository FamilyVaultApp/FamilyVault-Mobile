package com.github.familyvault.backend.client

import io.ktor.http.HttpStatusCode

class BackendBadResponseException(
    statusCode: HttpStatusCode,
    errorBody: String
) : RuntimeException("$statusCode - $errorBody")
